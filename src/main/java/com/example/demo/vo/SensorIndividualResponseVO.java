package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class SensorIndividualResponseVO implements Serializable {
    private List<StationResponseVO> individualStation;
    private Long startTime;
    private Long endTime;
}

