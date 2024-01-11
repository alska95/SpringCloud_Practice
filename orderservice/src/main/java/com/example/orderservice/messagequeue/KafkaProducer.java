package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @Value("${kafka.topic.order.catalog}")
    private String orderCatalogTopic;
    @Value("${kafka.topic.order.sync}")
    private String orderSyncTopic;

    private Object produceOrderMessage(String topic, Object dto) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(dto));
            log.info("Kafka Producer sent data from the Order microService" + dto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return dto;
    }

    public OrderDto produceOrderCatalogMessage(OrderDto orderDto) {
        return modelMapper.map(produceOrderMessage(orderCatalogTopic, orderDto), OrderDto.class);
    }

    public OrderDto produceOrderDbSyncMessage(OrderDto orderDto) {
        return modelMapper.map(produceOrderMessage(orderSyncTopic, orderDto), OrderDto.class);
    }
}
