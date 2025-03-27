package com.example.demo.service;

import com.example.demo.dto.SensorRecordDTO;
import com.example.demo.dto.request.SensorQueryDTO;
import com.example.demo.feign.response.AquarkResponse;
import com.example.demo.vo.SensorIndividualResponseVO;
import com.example.demo.vo.SensorSummaryResponseVO;

import java.util.List;

public interface SensorService {

    void processAndSave(List<SensorRecordDTO> sensorRecordDTOList);

   SensorSummaryResponseVO querySensorRecordSummary(SensorQueryDTO sensorQueryDTO);

    SensorIndividualResponseVO querySensorRecordIndividual(SensorQueryDTO sensorQueryDTO);

    List<SensorRecordDTO> transferDTO(List<AquarkResponse> responses);
}
