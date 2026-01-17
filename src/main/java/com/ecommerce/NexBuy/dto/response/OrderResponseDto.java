package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderTrackingNumber;
    private String totalQuantity;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
    private LocalDateTime deliveryDate;
    private String deliveryStatus;
    private String proofOfDelivery;
    private Boolean isReturned;
    private Set<OrderItemResponseDto> orderItems;
    private AddressResponseDto shippingAddress;
    private AddressResponseDto billingAddress;
    private CustomerResponseDto customer;
}
