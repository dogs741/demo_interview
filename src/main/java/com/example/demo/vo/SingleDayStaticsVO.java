package com.example.demo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SingleDayStaticsVO implements Serializable {
    private String date;
    private CalculateVO sum;
    private CalculateVO hoursAverage;
}

