package com.example.demo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class RecordStaticsVO implements Serializable {
    private List<SingleDayStaticsVO> singleDayStatics;
    private CalculateVO dailyAverage;
}

