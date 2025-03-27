package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AquarkSensorEnum {
    RAIN_D("RAIN_D", "rainD"),
    RH("RH", "rh"),
    TX("TX", "tx"),
    ECHO("ECHO", "echo"),
    SPEED("SPEED", "speed"),
    V1("V1", "v1"),
    V2("V2", "v2"),
    V3("V3", "v3"),
    V4("V4", "v4"),
    V5("V5", "v5"),
    V6("V6", "v6"),
    V7("V7", "v7");

    private final String name;
    private final String fieldName;
}
