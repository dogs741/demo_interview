package com.example.demo.po;

import com.example.demo.enums.AquarkSensorEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertSensor {
    @Id
    private Long id;
    private AquarkSensorEnum sensorName;
    private BigDecimal threshold;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
