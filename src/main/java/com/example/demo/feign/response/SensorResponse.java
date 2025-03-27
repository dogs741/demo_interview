package com.example.demo.feign.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SensorResponse implements Serializable {
    @JsonProperty("Volt")
    private VoltResponse volt;
    @JsonProperty("StickTxRh")
    private StickTxRhResponse stickTxRh;
    @JsonProperty("Ultrasonic_Level")
    private UltrasonicLevelResponse ultrasonicLevel;
    @JsonProperty("Water_speed_aquark")
    private WaterSpeedAquarkResponse waterSpeedAquark;
}
