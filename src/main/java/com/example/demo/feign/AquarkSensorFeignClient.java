package com.example.demo.feign;

import com.example.demo.feign.config.AquarkFeignConfiguration;
import com.example.demo.feign.response.AquarkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "aquark", url = "https://app.aquark.com.tw/api", configuration = AquarkFeignConfiguration.class)
public interface AquarkSensorFeignClient {

    @GetMapping("/raw/Angle2024/240627")
    AquarkResponse getApiFrom240627();

    @GetMapping("/raw/Angle2024/240706")
    AquarkResponse getApiFrom240706();

    @GetMapping("/raw/Angle2024/240708")
    AquarkResponse getApiFrom240708();

    @GetMapping("/raw/Angle2024/240709")
    AquarkResponse getApiFrom240709();

    @GetMapping("/raw/Angle2024/240710")
    AquarkResponse getApiFrom240710();
}
