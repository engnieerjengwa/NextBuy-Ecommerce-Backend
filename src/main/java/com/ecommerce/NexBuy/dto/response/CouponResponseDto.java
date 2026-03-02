package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponResponseDto {
    private Long id;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderAmount;
    private boolean valid;
    private String message;
    private BigDecimal discountAmount;
    private BigDecimal newTotal;
}
