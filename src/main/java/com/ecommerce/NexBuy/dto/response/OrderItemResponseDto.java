package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
    private Long productId;
}