package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequestDto {
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
    private Long productId;
}