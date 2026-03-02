package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReferralApplyRequestDto {
    @NotBlank(message = "Referral code is required")
    private String referralCode;
}
