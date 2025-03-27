package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CalculateVO implements Serializable {
    private BigDecimal rainD;
    private BigDecimal rh;
    private BigDecimal tx;
    private BigDecimal echo;
    private BigDecimal speed;
    private BigDecimal v1;
    private BigDecimal v2;
    private BigDecimal v3;
    private BigDecimal v4;
    private BigDecimal v5;
    private BigDecimal v6;
    private BigDecimal v7;
}

