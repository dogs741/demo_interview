package com.example.demo.controller;

import com.example.demo.dto.ResponseBean;
import com.example.demo.dto.request.SensorQueryDTO;
import com.example.demo.service.SensorService;
import com.example.demo.vo.SensorIndividualResponseVO;
import com.example.demo.vo.SensorSummaryResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "觀測資訊查詢")
@RequestMapping("/station-statics")
@RestController
public class SensorQueryController {
    @Autowired
    private SensorService sensorService;

    @Operation(summary = "不區分站點的所有資料")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到資料", content = {@Content(mediaType = "application/json")})})
    @PostMapping("/summary")
    public ResponseEntity<ResponseBean> querySensorSummaryRecords(@Parameter(description = "查詢條件") @RequestBody SensorQueryDTO queryDTO) {
        SensorSummaryResponseVO result = sensorService.querySensorRecordSummary(queryDTO);
        if (result == null)
            return ResponseEntity.ok(ResponseBean.builder().code("200").message("Data Not Found").build());
        return ResponseEntity.ok(ResponseBean.builder().code("200").message("OK").data(result).build());
    }

    @Operation(summary = "區分站點的所有資料")
    @PostMapping("/individual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到資料", content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<ResponseBean> querySensorIndividualRecords(@Parameter(description = "查詢條件") @RequestBody SensorQueryDTO queryDTO) {
        SensorIndividualResponseVO result = sensorService.querySensorRecordIndividual(queryDTO);
        if (result == null)
            return ResponseEntity.ok(ResponseBean.builder().code("200").message("Data Not Found").build());
        return ResponseEntity.ok(ResponseBean.builder().code("200").message("OK").data(result).build());
    }
}
