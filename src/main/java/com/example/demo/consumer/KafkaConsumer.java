package com.example.demo.consumer;

import com.example.demo.config.KafkaConfig;
import com.example.demo.dto.AlertApiDTO;
import com.example.demo.dto.request.AlertSensorDTO;
import com.example.demo.service.AlertService;
import com.example.demo.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class KafkaConsumer {

    @Autowired
    private AlertService alertService;

    @KafkaListener(id = "kafka", topics = KafkaConfig.SENSOR_TOPIC, containerFactory = "sensorContainerFactory")
    public void handleSensor(AlertSensorDTO alertSensorDTO) {
        log.info("KafkaConsumer::handleSensor received message from topic: {}, message: {}", KafkaConfig.SENSOR_TOPIC, JsonUtils.toString(alertSensorDTO));
        alertService.processAlertSensorThreshold(alertSensorDTO);
    }

    @KafkaListener(id = "kafka", topics = KafkaConfig.ALERT_TOPIC, containerFactory = "alertContainerFactory")
    public void handleAlertApi(AlertApiDTO alertApiDTO) {
        // submit alert api ... to do something more
        log.warn("Sensor Alert : reach the limit, sensor name {}, threshold: {}, station details: {}", alertApiDTO.getSensorName().getName(), alertApiDTO.getThreshold(), JsonUtils.toString(alertApiDTO));
    }
}
