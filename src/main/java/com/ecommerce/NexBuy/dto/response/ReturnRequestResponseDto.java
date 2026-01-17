package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ReturnRequestResponseDto {
    private Long id;
    private Long orderId;
    private String orderTrackingNumber;
    private String returnReason;
    private String comments;
    private String status;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private Set<ReturnItemResponseDto> returnItems;
}