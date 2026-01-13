package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderRequestDto {
    private String orderTrackingNumber;
    private String totalQuantity;
    private BigDecimal totalPrice;
    private String status;
    private Set<OrderItemRequestDto> orderItems;
    private AddressRequestDto shippingAddress;
    private AddressRequestDto billingAddress;
    private Long customerId;
}