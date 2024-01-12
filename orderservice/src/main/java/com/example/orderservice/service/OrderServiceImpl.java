package com.example.orderservice.service;


import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducerService;
import com.example.orderservice.repository.OrderRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(OrderRepository orderRepository, KafkaProducerService kafkaProducerService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQuantity() * orderDto.getUnitPrice());

//        OrderEntity orderEntity = modelMapper.map(orderDto , OrderEntity.class);
//        orderEntity.setCreatedAt(new Date());
//        orderRepository.save(orderEntity);
//        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);

        kafkaProducerService.produceOrderCatalogMessage(orderDto);
        kafkaProducerService.produceOrderDbSyncMessage(orderDto);
        return orderDto;
    }

    @Override
    public List<OrderEntity> getOrderListByUserId(String userId) {
        return StreamSupport.stream(orderRepository.findByUserId(userId).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Iterable<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
