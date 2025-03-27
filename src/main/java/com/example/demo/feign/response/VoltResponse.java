package com.example.demo.feign.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class VoltResponse implements Serializable {
    private BigDecimal v1;
    private BigDecimal v2;
    private BigDecimal v3;
    private BigDecimal v4;
    private BigDecimal v5;
    private BigDecimal v6;
    private BigDecimal v7;
}
