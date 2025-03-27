package com.example.demo.dto.request;

import com.example.demo.enums.AquarkSensorEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlertSensorDTO {
    @NotNull
    private AquarkSensorEnum sensorName;
    @NotNull
    private BigDecimal threshold;
}
