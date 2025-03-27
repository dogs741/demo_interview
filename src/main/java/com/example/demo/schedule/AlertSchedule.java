package com.example.demo.schedule;

import com.example.demo.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertSchedule {

    @Autowired
    AlertService alertService;

    @Scheduled(cron = "* * 3 ? * ?")
    public void scheduleTask() {
        log.info("AlertSchedule::scheduleTask starts");
        long start = System.currentTimeMillis();
        alertService.scheduleAlertSensorThreshold();
        long end = System.currentTimeMillis();
        log.info("AlertSchedule::scheduleTask end, cost: {}s", (end - start) / 1000);
    }
}
