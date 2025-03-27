package com.example.demo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SensorSummaryResponseVO extends BaseSensorResponseVO {
    private Long startTime;
    private Long endTime;

    public SensorSummaryResponseVO(Long startTime, Long endTime, BaseSensorResponseVO responseVO) {
        super(responseVO.getPeekTimeRecords(), responseVO.getNonPeekTimeRecords(), responseVO.getStatics());
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

