package com.example.demo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StationResponseVO extends BaseSensorResponseVO {
    private String stationId;

    public StationResponseVO(String stationId, BaseSensorResponseVO responseVO) {
        super(responseVO.getPeekTimeRecords(), responseVO.getNonPeekTimeRecords(), responseVO.getStatics());
        this.stationId = stationId;
    }
}

