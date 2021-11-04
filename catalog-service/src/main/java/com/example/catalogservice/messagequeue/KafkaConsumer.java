package com.example.catalogservice.messagequeue;

import com.example.catalogservice.domain.CatalogEntity;
import com.example.catalogservice.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    final CatalogRepository catalogRepository;

    public KafkaConsumer(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @KafkaListener(topics = "example-catalog-topic") //해당 토픽의 변경을 캐치
    public void updateQuantity(String kafkaMessage){
        log.info("kafaka Message: --> "+ kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        }catch (JsonProcessingException ex){
            ex.printStackTrace();;
        }
        CatalogEntity targetProduct = catalogRepository.findByProductId((String) map.get("productId"));
        if(targetProduct != null){
            targetProduct.setStock(targetProduct.getStock() - (Integer)map.get("qty"));
        }
    }


}
