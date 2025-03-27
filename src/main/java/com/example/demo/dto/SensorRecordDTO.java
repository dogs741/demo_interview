package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class SensorRecordDTO implements Serializable {

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

    public SensorRecordDTO() {
    }

    public static SensorRecordDTO getDefaultZeroDTO() {
        return SensorRecordDTO.builder()
                .rainD(BigDecimal.ZERO)
                .rh(BigDecimal.ZERO)
                .tx(BigDecimal.ZERO)
                .echo(BigDecimal.ZERO)
                .speed(BigDecimal.ZERO)
                .v1(BigDecimal.ZERO)
                .v2(BigDecimal.ZERO)
                .v3(BigDecimal.ZERO)
                .v4(BigDecimal.ZERO)
                .v5(BigDecimal.ZERO)
                .v6(BigDecimal.ZERO)
                .v7(BigDecimal.ZERO)
                .build();
    }
}

