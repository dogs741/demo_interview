package com.example.demo.feign.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WaterSpeedAquarkResponse implements Serializable {
    private BigDecimal speed;
}
