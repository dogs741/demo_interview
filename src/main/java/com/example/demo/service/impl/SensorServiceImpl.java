package com.example.demo.service.impl;

import com.example.demo.dto.SensorRecordDTO;
import com.example.demo.dto.request.SensorQueryDTO;
import com.example.demo.feign.response.*;
import com.example.demo.mapper.SensorRecordMapper;
import com.example.demo.po.SensorRecord;
import com.example.demo.service.SensorService;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.JsonUtils;
import com.example.demo.vo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SensorServiceImpl implements SensorService {
    @Autowired
    private SensorRecordMapper recordMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Function<ObserverResponse, SensorRecordDTO> transferResponseToDTO = response -> {
        SensorRecordDTO dto = SensorRecordDTO.builder()
                .stationId(response.getStationId())
                .obsTime(DateUtils.parseLocalDateTime(response.getObsTime(), "yyyy-MM-dd HH:mm:ss"))
                .csq(response.getCsq())
                .rainD(response.getRainD())
                .build();
        SensorResponse sensorResponse = response.getSensor();
        if (sensorResponse != null) {
            VoltResponse volt = sensorResponse.getVolt();
            StickTxRhResponse stickTxRh = sensorResponse.getStickTxRh();
            UltrasonicLevelResponse ultrasonicLevel = sensorResponse.getUltrasonicLevel();
            WaterSpeedAquarkResponse waterSpeedAquark = sensorResponse.getWaterSpeedAquark();
            if (stickTxRh != null) {
                dto.setRh(stickTxRh.getRh());
                dto.setTx(stickTxRh.getTx());
            }

            if (ultrasonicLevel != null) {
                dto.setEcho(ultrasonicLevel.getEcho());
            }

            if (waterSpeedAquark != null && waterSpeedAquark.getSpeed() != null) {
                dto.setSpeed(waterSpeedAquark.getSpeed().abs());
            }

            if (volt != null) {
                dto.setV1(volt.getV1());
                dto.setV2(volt.getV2());
                dto.setV3(volt.getV3());
                dto.setV4(volt.getV4());
                dto.setV5(volt.getV5());
                dto.setV6(volt.getV6());
                dto.setV7(volt.getV7());
            }
        }
        return dto;
    };

    private final Function<SensorRecordDTO, SensorRecord> transferDTOToPO = dto -> {
        SensorRecord sensorRecord = new SensorRecord();
        BeanUtils.copyProperties(dto, sensorRecord);
        return sensorRecord;
    };

    private final Function<SensorRecord, SensorRecordDTO> transferPOToDTO = po -> {
        SensorRecordDTO sensorRecordDTO = SensorRecordDTO.builder().build();
        BeanUtils.copyProperties(po, sensorRecordDTO);
        return sensorRecordDTO;
    };

    private final Function<SensorRecordDTO, CalculateVO> transferDTOToVO = dto -> {
        CalculateVO calculateVO = new CalculateVO();
        BeanUtils.copyProperties(dto, calculateVO);
        return calculateVO;
    };

    private final Function<SensorRecordDTO, LocalDate> groupingByObsTime = dto -> dto.getObsTime().toLocalDate();

    @Transactional
    @Override
    public void processAndSave(List<SensorRecordDTO> sensorRecordDTOList) {
        log.info("SensorServiceImpl::processAndSave sensorRecordsList: {}", JsonUtils.toString(sensorRecordDTOList));
        recordMapper.batchInsert(sensorRecordDTOList.stream().map(transferDTOToPO).toList());
    }

    @Override
    public SensorSummaryResponseVO querySensorRecordSummary(SensorQueryDTO sensorQueryDTO) {
        log.info("SensorServiceImpl::querySensorRecordSummary request: {}", JsonUtils.toString(sensorQueryDTO));
        Long startTime = DateUtils.getMilliseconds(sensorQueryDTO.getStartDate());
        Long endTime = DateUtils.getMilliseconds(sensorQueryDTO.getEndDate());
        List<SensorRecord> sensorRecords = getSensorRecords(sensorQueryDTO);
        if (sensorRecords.isEmpty()) {
            log.info("SensorServiceImpl::querySensorRecordSummary data not found: {}", JsonUtils.toString(sensorQueryDTO));
            return null;
        }
        return new SensorSummaryResponseVO(startTime, endTime, processResponseVO(sensorRecords));
    }

    @Override
    public SensorIndividualResponseVO querySensorRecordIndividual(SensorQueryDTO sensorQueryDTO) {
        log.info("SensorServiceImpl::querySensorRecordIndividual request: {}", JsonUtils.toString(sensorQueryDTO));
        List<SensorRecord> sensorRecords = getSensorRecords(sensorQueryDTO);

        if (sensorRecords.isEmpty()) {
            log.info("SensorServiceImpl::querySensorRecordIndividual data not found: {}", JsonUtils.toString(sensorQueryDTO));
            return null;
        }

        LinkedHashMap<String, List<SensorRecord>> map = sensorRecords.stream().collect(Collectors.groupingBy(SensorRecord::getStationId, LinkedHashMap::new, Collectors.toList()));

        List<StationResponseVO> individualStation = new ArrayList<>();
        map.forEach((k, v) -> {
            individualStation.add(new StationResponseVO(k, processResponseVO(v)));
        });

        Long startTime = DateUtils.getMilliseconds(sensorQueryDTO.getStartDate());
        Long endTime = DateUtils.getMilliseconds(sensorQueryDTO.getEndDate());

        return new SensorIndividualResponseVO(individualStation, startTime, endTime);
    }

    @Override
    public List<SensorRecordDTO> transferDTO(List<AquarkResponse> responses) {
        return responses.stream()
                .flatMap(re -> Optional.of(re.getRaw()).orElse(new ArrayList<>()).stream())
                .map(transferResponseToDTO).toList();
    }

    private List<SensorRecord> getSensorRecords(SensorQueryDTO sensorQueryDTO) {
        List<SensorRecord> redisRecords = new ArrayList<>();
        AtomicInteger stationResultCount = new AtomicInteger();
        sensorQueryDTO.getStationIds().forEach(stationId -> {
            Set<String> redisRangeData = stringRedisTemplate.opsForZSet().rangeByScore(stationId, DateUtils.getMilliseconds(sensorQueryDTO.getStartDate()), DateUtils.getMilliseconds(sensorQueryDTO.getEndDate()));
            if (redisRangeData != null) {
                stationResultCount.getAndIncrement();
                redisRangeData.forEach(json -> {
                    List<SensorRecord> temp = JsonUtils.parse(json, new TypeReference<>() {
                    });
                    if (temp != null) redisRecords.addAll(temp);
                });
            }
        });

        if (stationResultCount.get() < sensorQueryDTO.getStationIds().size()) {
            List<SensorRecord> sensorRecords = recordMapper.getRecordsByArguments(sensorQueryDTO.getStationIds(), sensorQueryDTO.getStartDate(), sensorQueryDTO.getEndDate());
            if (sensorRecords == null || sensorRecords.isEmpty()) return new ArrayList<>();
            LinkedHashMap<String, List<SensorRecord>> map = sensorRecords.stream().collect(Collectors.groupingBy(SensorRecord::getStationId, LinkedHashMap::new, Collectors.toList()));
            // updateRedis
            map.forEach((key, list) ->
                    list.stream()
                            .collect(Collectors.groupingBy(po -> DateUtils.getMilliseconds(po.getObsTime())))
                            .forEach((time, records) -> stringRedisTemplate.opsForZSet().add(key, JsonUtils.toString(records), time)));
            return sensorRecords;
        }
        return redisRecords;
    }

    private BaseSensorResponseVO processResponseVO(List<SensorRecord> records) {
        List<SensorRecordDTO> recordDTOS = records.stream().map(transferPOToDTO).toList();
        List<SensorRecordDTO> peekTimeRecords = recordDTOS.stream().filter(dto -> {
            if (dto.getObsTime() != null) {
                LocalDateTime obsTime = dto.getObsTime();
                return checkIsPeekTimeOrNot(obsTime);
            }
            return false;
        }).toList();

        List<SensorRecordDTO> nonPeekTimeRecords = recordDTOS.stream().filter(dto -> {
            if (dto.getObsTime() != null) {
                LocalDateTime obsTime = dto.getObsTime();
                return !checkIsPeekTimeOrNot(obsTime);
            }
            return false;
        }).toList();

        return BaseSensorResponseVO.builder()
                .peekTimeRecords(peekTimeRecords)
                .nonPeekTimeRecords(nonPeekTimeRecords)
                .statics(formatStatics(recordDTOS))
                .build();
    }

    private boolean checkIsPeekTimeOrNot(LocalDateTime time) {
        int day = time.getDayOfWeek().getValue();
        LocalTime checkTime = LocalTime.of(time.getHour(), time.getMinute(), time.getSecond());
//        尖峰時間（週一～週三 : 7:30 ~17:30。 週四 週五全天）
//        離峰時間（週一～週三 : 00:00 ~7:30 週一～週三 17:30~00:00。 週六 週日 全天
        LocalTime peekStart = LocalTime.of(7, 30);
        LocalTime peekEnd = LocalTime.of(17, 30);

        return (day == 4 || day == 5) || (1 <= day && day <= 3 && checkTime.isAfter(peekStart) && checkTime.isBefore(peekEnd));
    }

    private RecordStaticsVO formatStatics(List<SensorRecordDTO> recordDTOS) {
        // every single day
        LinkedHashMap<LocalDate, SensorRecordDTO> singleDaySumMap = getSingleDaySumMap(recordDTOS);
        // hours average
        Map<LocalDate, SensorRecordDTO> hoursMap = getHoursMap(singleDaySumMap);
        List<SingleDayStaticsVO> result = new ArrayList<>();
        // daily average
        SensorRecordDTO dailyRecord = getDailyRecord(singleDaySumMap);

        return generateStaticsVO(singleDaySumMap, result, hoursMap, dailyRecord);
    }

    private RecordStaticsVO generateStaticsVO(LinkedHashMap<LocalDate, SensorRecordDTO> singleDaySumMap, List<SingleDayStaticsVO> result, Map<LocalDate, SensorRecordDTO> hoursMap, SensorRecordDTO dailyRecord) {
        singleDaySumMap.keySet().forEach(k ->
                result.add(SingleDayStaticsVO.builder()
                        .date(DateUtils.getMilliseconds(k).toString())
                        .sum(transferDTOToVO.apply(singleDaySumMap.get(k)))
                        .hoursAverage(transferDTOToVO.apply(hoursMap.get(k)))
                        .build()));

        return RecordStaticsVO.builder()
                .singleDayStatics(result)
                .dailyAverage(transferDTOToVO.apply(dailyRecord))
                .build();
    }

    private static SensorRecordDTO getDailyRecord(LinkedHashMap<LocalDate, SensorRecordDTO> singleDaySumMap) {
        return singleDaySumMap.values().stream().reduce(SensorRecordDTO.getDefaultZeroDTO(), (a, b) -> {
            a.setRainD(a.getRainD().add(b.getRainD()));
            a.setRh(a.getRh().add(b.getRh()));
            a.setTx(a.getTx().add(b.getTx()));
            a.setEcho(a.getEcho().add(b.getEcho()));
            a.setSpeed(a.getSpeed().add(b.getSpeed()));
            a.setV1(a.getV1().add(b.getV1()));
            a.setV2(a.getV2().add(b.getV2()));
            a.setV3(a.getV3().add(b.getV3()));
            a.setV4(a.getV4().add(b.getV4()));
            a.setV5(a.getV5().add(b.getV5()));
            a.setV6(a.getV6().add(b.getV6()));
            a.setV7(a.getV7().add(b.getV7()));
            return a;
        });
    }

    private static Map<LocalDate, SensorRecordDTO> getHoursMap(LinkedHashMap<LocalDate, SensorRecordDTO> singleDaySumMap) {
        Map<LocalDate, SensorRecordDTO> hoursMap = new LinkedHashMap<>();
        singleDaySumMap.forEach((k, v) -> {
            SensorRecordDTO dto = SensorRecordDTO.builder()
                    .rainD(v.getRainD().divide(new BigDecimal(24), new MathContext(4)))
                    .rh(v.getRh().divide(new BigDecimal(24), new MathContext(4)))
                    .tx(v.getTx().divide(new BigDecimal(24), new MathContext(4)))
                    .echo(v.getEcho().divide(new BigDecimal(24), new MathContext(4)))
                    .speed(v.getSpeed().divide(new BigDecimal(24), new MathContext(4)))
                    .v1(v.getV1().divide(new BigDecimal(24), new MathContext(4)))
                    .v2(v.getV2().divide(new BigDecimal(24), new MathContext(4)))
                    .v3(v.getV3().divide(new BigDecimal(24), new MathContext(4)))
                    .v4(v.getV4().divide(new BigDecimal(24), new MathContext(4)))
                    .v5(v.getV5().divide(new BigDecimal(24), new MathContext(4)))
                    .v6(v.getV6().divide(new BigDecimal(24), new MathContext(4)))
                    .v7(v.getV7().divide(new BigDecimal(24), new MathContext(4)))
                    .build();
            hoursMap.put(k, dto);
        });
        return hoursMap;
    }

    private LinkedHashMap<LocalDate, SensorRecordDTO> getSingleDaySumMap(List<SensorRecordDTO> recordDTOS) {
        return recordDTOS.stream().collect(Collectors.groupingBy(groupingByObsTime, LinkedHashMap::new, Collectors.reducing(SensorRecordDTO.getDefaultZeroDTO(), (a, b) -> {
            a.setRainD(a.getRainD().add(b.getRainD()));
            a.setRh(a.getRh().add(b.getRh()));
            a.setTx(a.getTx().add(b.getTx()));
            a.setEcho(a.getEcho().add(b.getEcho()));
            a.setSpeed(a.getSpeed().add(b.getSpeed()));
            a.setV1(a.getV1().add(b.getV1()));
            a.setV2(a.getV2().add(b.getV2()));
            a.setV3(a.getV3().add(b.getV3()));
            a.setV4(a.getV4().add(b.getV4()));
            a.setV5(a.getV5().add(b.getV5()));
            a.setV6(a.getV6().add(b.getV6()));
            a.setV7(a.getV7().add(b.getV7()));
            return a;
        })));
    }
}
