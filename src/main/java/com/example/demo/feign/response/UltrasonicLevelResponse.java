package com.example.demo.feign.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UltrasonicLevelResponse implements Serializable {
    private BigDecimal echo;

}
