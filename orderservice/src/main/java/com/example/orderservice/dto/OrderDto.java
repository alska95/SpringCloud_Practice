package com.example.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderDto {
    private String productId;
    @JsonProperty("qty")
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
