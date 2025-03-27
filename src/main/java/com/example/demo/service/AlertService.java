package com.example.demo.service;

import com.example.demo.dto.request.AlertSensorDTO;

public interface AlertService {
    void sensorAlertSettings(AlertSensorDTO alertSensorDTO);

    void processAlertSensorThreshold(AlertSensorDTO alertSensorDTO);

    void scheduleAlertSensorThreshold();
}
