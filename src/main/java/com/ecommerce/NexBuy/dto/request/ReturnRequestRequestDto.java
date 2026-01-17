package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class ReturnRequestRequestDto {
    private Long orderId;
    private String returnReason;
    private String comments;
    private Set<ReturnItemRequestDto> returnItems;
}