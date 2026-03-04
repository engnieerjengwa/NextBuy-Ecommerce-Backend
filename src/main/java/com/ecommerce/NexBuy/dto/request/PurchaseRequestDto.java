package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class PurchaseRequestDto {

    @NotNull(message = "Customer information is required")
    @Valid
    private CustomerRequestDto customer;

    @NotNull(message = "Billing address is required")
    @Valid
    private AddressRequestDto billingAddress;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressRequestDto shippingAddress;

    @NotNull(message = "Order information is required")
    @Valid
    private OrderSummaryDto order;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private Set<OrderItemRequestDto> orderItems;

    /**
     * Lightweight order summary sent from the frontend.
     * Only contains totalPrice and totalQuantity (no tracking number, status, etc.)
     */
    @Data
    public static class OrderSummaryDto {
        @NotNull(message = "Total price is required")
        private BigDecimal totalPrice;

        private int totalQuantity;

        private String paymentIntentId;
    }
}
