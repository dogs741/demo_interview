package com.example.demo.service.impl;

import com.example.demo.config.KafkaConfig;
import com.example.demo.dto.AlertApiDTO;
import com.example.demo.dto.request.AlertSensorDTO;
import com.example.demo.enums.AquarkSensorEnum;
import com.example.demo.mapper.AlertSensorMapper;
import com.example.demo.mapper.SensorRecordMapper;
import com.example.demo.mapper.StationMapper;
import com.example.demo.po.AlertSensor;
import com.example.demo.po.SensorRecord;
import com.example.demo.po.Station;
import com.example.demo.service.AlertService;
import com.example.demo.service.KafkaService;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private AlertSensorMapper alertSensorMapper;
    @Autowired
    private SensorRecordMapper recordMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private KafkaService kafkaService;

    @Transactional
    @Override
    public void sensorAlertSettings(AlertSensorDTO alertSensorDTO) {
        log.info("AlertServiceImpl::sensorAlertProcess request: {}", JsonUtils.toString(alertSensorDTO));
        AlertSensor alertSensor = new AlertSensor();
        alertSensor.setSensorName(alertSensorDTO.getSensorName());
        alertSensor.setThreshold(alertSensorDTO.getThreshold());
        alertSensorMapper.insert(alertSensor);
        stringRedisTemplate.opsForValue().set(alertSensorDTO.getSensorName().getName(), alertSensorDTO.getThreshold().toPlainString());
        kafkaService.pushToTopic(KafkaConfig.SENSOR_TOPIC, alertSensorDTO);
    }

    @Override
    public void processAlertSensorThreshold(AlertSensorDTO alertSensorDTO) {
        BigDecimal threshold = getThreshold(alertSensorDTO);
        if (threshold == null) return;

        processRedisStatics(getStaticsJsonString(LocalDateTime.now(), LocalDateTime.now().plusDays(1)), alertSensorDTO.getSensorName(), threshold);
    }

    private BigDecimal getThreshold(AlertSensorDTO alertSensorDTO) {
        String value = stringRedisTemplate.opsForValue().get(alertSensorDTO.getSensorName().getName());
        if (value != null) return new BigDecimal(value);
        AlertSensor alertSensor = alertSensorMapper.getLatestRecordBySensorName(alertSensorDTO.getSensorName());
        if (alertSensor == null) {
            log.warn("AlertServiceImpl::getThreshold threshold not found in. redis and mysql, sensorName: {}", alertSensorDTO.getSensorName().getFieldName());
            return null;
        }
        return alertSensor.getThreshold();
    }

    private List<String> getStaticsJsonString(LocalDateTime startTime, LocalDateTime endTime) {
        List<Station> stationList = stationMapper.getAllStations();
        List<String> statics = new ArrayList<>();
        stationList.forEach(s -> {
            Set<String> redisStatics = stringRedisTemplate.opsForZSet().rangeByScore(s.getStationId(), DateUtils.getMilliseconds(startTime), DateUtils.getMilliseconds(endTime));
            if (redisStatics == null || redisStatics.isEmpty()) return;
            statics.addAll(redisStatics);
        });
        return statics;
    }

    private void processRedisStatics(List<String> redisStaticData, AquarkSensorEnum sensorName, BigDecimal threshold) {
        redisStaticData.forEach(json -> {
            List<SensorRecord> sensorRecords = JsonUtils.parse(json, new TypeReference<>() {
            });
            verifyThreshold(sensorName, threshold, sensorRecords);
        });
    }

    @Override
    public void scheduleAlertSensorThreshold() {
        List<AlertSensor> alertSensors = alertSensorMapper.getAllRecord();
        List<Station> stationList = stationMapper.getAllStations();
        alertSensors.forEach(alert -> {
            List<SensorRecord> sensorRecords = recordMapper.getRecordsByArguments(stationList.stream().map(Station::getStationId).toList(), alert.getUpdateDate(), LocalDateTime.now());
            verifyThreshold(alert.getSensorName(), alert.getThreshold(), sensorRecords);
        });
    }

    private void verifyThreshold(AquarkSensorEnum sensorName, BigDecimal threshold, List<SensorRecord> sensorRecords) {
        if (sensorRecords == null || sensorRecords.isEmpty()) return;
        sensorRecords.forEach(sensorRecord -> {
            try {
                Field targetField = sensorRecord.getClass().getField(sensorName.getFieldName());
                targetField.setAccessible(true);
                BigDecimal staticsValue = (BigDecimal) targetField.get(sensorRecord);
                if (threshold.compareTo(staticsValue) < 0) {
                    AlertApiDTO alertApiDTO = new AlertApiDTO();
                    BeanUtils.copyProperties(sensorRecord, alertApiDTO);
                    alertApiDTO.setSensorName(sensorName);
                    alertApiDTO.setThreshold(threshold);
                    kafkaService.pushToTopic(KafkaConfig.ALERT_TOPIC, alertApiDTO);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("AlertServiceImpl::verifyThreshold get sensorRecord reflection error, target field name: {}, error: ", sensorName.getFieldName(), e);
            }
        });
    }
}
