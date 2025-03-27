package com.example.demo.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Station {
    @Id
    private Long id;
    private String stationId;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
