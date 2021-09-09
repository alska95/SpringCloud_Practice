package com.example.orderservice.controller;

import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service") //게이트웨이에서 붙는 prefix
public class OrderController {
    final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }

    @GetMapping("/{userId}/orders") //requestparam은 뒤에 ? 쿼리로 가져오는것 햇갈리지말자
    public ResponseEntity<List<ResponseOrder>> getOrder(
            @PathVariable String userId) {

        Iterable<OrderEntity> ordersByOrderId = orderService.getOrdersByOrderId(userId);
        ModelMapper mapper = new ModelMapper();
        List<ResponseOrder> responseOrders = new ArrayList<>();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ordersByOrderId.forEach(v ->{
            responseOrders.add(mapper.map(v , ResponseOrder.class));

        });

        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }
}
