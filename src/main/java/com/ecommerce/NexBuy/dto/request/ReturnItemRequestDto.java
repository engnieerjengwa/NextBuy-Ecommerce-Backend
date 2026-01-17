package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

@Data
public class ReturnItemRequestDto {
    private Long orderItemId;
    private int quantity;
    private String reason;
}