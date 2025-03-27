package com.example.demo.dto;

import com.example.demo.enums.AquarkSensorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlertApiDTO extends SensorRecordDTO {

    private AquarkSensorEnum sensorName;
    private BigDecimal threshold;

    public AlertApiDTO() {
    }

}
