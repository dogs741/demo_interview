package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SensorQueryDTO {
    @NotNull
    private List<String> stationIds;
    @NotNull
    private LocalDateTime startDate;
    private LocalDateTime endDate = LocalDateTime.now();
}
