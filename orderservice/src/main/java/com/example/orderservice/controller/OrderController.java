package com.example.orderservice.controller;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/order-service") //게이트웨이에서 붙는 prefix
@RequestMapping("") //게이트웨이에 segment적용
public class OrderController {
    final OrderService orderService;
    final KafkaProducer kafkaProducer;

    public OrderController(OrderService orderService, KafkaProducer kafkaProducer) {
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request){
        return String.format("It's working in Order Service on Port %s", request.getLocalPort());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder order, @PathVariable String userId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = mapper.map(order , OrderDto.class);
        orderDto.setUserId(userId);
        orderService.createOrder(orderDto);
        ResponseOrder responseOrder = mapper.map(orderDto , ResponseOrder.class);

        /* send this order to the kafka*/
        kafkaProducer.send("example-catalog-topic" , orderDto);


        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable String userId) {

        Iterable<OrderEntity> ordersByOrderId = orderService.getOrdersByUserId(userId);
        ModelMapper mapper = new ModelMapper();
        List<ResponseOrder> responseOrders = new ArrayList<>();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ordersByOrderId.forEach(v ->{
            responseOrders.add(mapper.map(v , ResponseOrder.class));
            log.info(v.getProductId());
        });

        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }
}
