package com.example.demo.service.impl;

import com.example.demo.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void pushToTopic(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
