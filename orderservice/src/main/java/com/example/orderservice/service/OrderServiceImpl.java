package com.example.orderservice.service;


import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.repository.OrderRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Data
@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDetails) {
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQuantity() * orderDetails.getUnitPrice());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = mapper.map(orderDetails , OrderEntity.class);
        Date date = new Date();
        orderEntity.setCreatedAt(date);
        orderRepository.save(orderEntity);
        OrderDto retValue = mapper.map(orderEntity , OrderDto.class);
        return retValue;
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> byOrderId = orderRepository.findByUserId(userId);
        return byOrderId;
    }

    @Override
    public Iterable<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }
}
