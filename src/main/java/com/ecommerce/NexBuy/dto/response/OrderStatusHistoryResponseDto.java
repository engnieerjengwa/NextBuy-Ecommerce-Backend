package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderStatusHistoryResponseDto {

    private Long id;
    private String status;
    private String note;
    private LocalDateTime createdAt;
}
