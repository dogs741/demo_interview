package com.example.demo.schedule;

import com.example.demo.feign.AquarkSensorFeignClient;
import com.example.demo.feign.response.AquarkResponse;
import com.example.demo.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ObserveSchedule {

    @Autowired
    SensorService sensorService;
    @Autowired
    AquarkSensorFeignClient client;

    @Scheduled(cron = "* * 0 ? * ?")
    public void scheduleTask() {
        try {
            log.info("ObserveSchedule::scheduleTask starts");
            long start = System.currentTimeMillis();
            List<AquarkResponse> respnseList = new ArrayList<>();
            respnseList.add(client.getApiFrom240627());
            respnseList.add(client.getApiFrom240706());
            respnseList.add(client.getApiFrom240708());
            respnseList.add(client.getApiFrom240709());
            respnseList.add(client.getApiFrom240710());
            sensorService.processAndSave(sensorService.transferDTO(respnseList));
            long end = System.currentTimeMillis();
            log.info("ObserveSchedule::scheduleTask end, cost: {}s", (end - start) / 1000);
        } catch (Exception e) {
            log.error("ObserveSchedule::scheduleTask error: ", e);
        }
    }
}
