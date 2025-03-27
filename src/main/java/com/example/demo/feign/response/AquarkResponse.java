package com.example.demo.feign.response;

import lombok.Data;

import java.util.List;

@Data
public class AquarkResponse {
   List<ObserverResponse> raw;
}
