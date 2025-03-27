package com.example.demo.feign.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ObserverResponse implements Serializable {
    @JsonProperty("station_id")
    private String stationId;
    @JsonProperty("obs_time")
    private String obsTime;
    @JsonProperty("CSQ")
    private String csq;
    @JsonProperty("rain_D")
    private BigDecimal rainD;
    private SensorResponse sensor;
}