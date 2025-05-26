package com.example.demo.controller;

import com.example.demo.dto.ResponseBean;
import com.example.demo.dto.request.AlertSensorDTO;
import com.example.demo.service.AlertService;
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
@Tag(name = "告警設定")
@RequestMapping("/alert")
@RestController
public class SensorAlertController {
    @Autowired
    private AlertService alertService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "設定臨界值", content = {@Content(mediaType = "application/json")})})
    @PostMapping
    public ResponseEntity<ResponseBean> settingSensorAlertThreshold(@Parameter(description = "設定條件") @RequestBody AlertSensorDTO alertSensorDTO) {
        alertService.sensorAlertSettings(alertSensorDTO);
        return ResponseEntity.ok(ResponseBean.builder().code("200").message("OK").build());
    }
}
