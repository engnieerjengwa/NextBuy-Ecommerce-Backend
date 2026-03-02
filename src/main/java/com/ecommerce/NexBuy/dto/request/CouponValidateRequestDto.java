package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CouponValidateRequestDto {
    @NotBlank(message = "Coupon code is required")
    private String code;
}
