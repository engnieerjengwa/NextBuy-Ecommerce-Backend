package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoyaltyRedeemRequestDto {
    @NotNull(message = "Points to redeem is required")
    @Positive(message = "Points must be positive")
    private Integer points;
}
