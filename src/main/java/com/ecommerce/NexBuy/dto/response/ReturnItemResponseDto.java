package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

@Data
public class ReturnItemResponseDto {
    private Long id;
    private OrderItemResponseDto orderItem;
    private int quantity;
    private String reason;
}