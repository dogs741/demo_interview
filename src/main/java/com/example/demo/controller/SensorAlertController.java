package com.example.demo.controller;

import com.example.demo.dto.ResponseBean;
import com.example.demo.dto.request.AlertSensorDTO;
import com.example.demo.schedule.AlertSchedule;
import com.example.demo.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/alert")
@RestController
public class SensorAlertController {
    @Autowired
    AlertService alertService;
    @Autowired
    AlertSchedule alertSchedule;

    @PostMapping
    public ResponseEntity<ResponseBean> settingSensorAlertThreshold(@RequestBody AlertSensorDTO alertSensorDTO) {
        alertService.sensorAlertSettings(alertSensorDTO);
        return ResponseEntity.ok(ResponseBean.builder().code("200").message("OK").build());
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseBean> testAlert() {
        alertSchedule.scheduleTask();
        return ResponseEntity.ok(ResponseBean.builder().code("200").message("OK").build());
    }
}
