package com.example.demo.service;

public interface KafkaService {
    void pushToTopic(String topic, Object message);
}
