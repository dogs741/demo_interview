package com.example.demo.vo;

import com.example.demo.dto.SensorRecordDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSensorResponseVO implements Serializable {
    private List<SensorRecordDTO> peekTimeRecords;
    private List<SensorRecordDTO> nonPeekTimeRecords;
    private RecordStaticsVO statics;
}

