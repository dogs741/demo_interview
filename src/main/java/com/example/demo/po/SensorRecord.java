package com.example.demo.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SensorRecord {
    @Id
    private Long id;
    private String stationId;
    private LocalDateTime obsTime;
    private String csq;

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

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
