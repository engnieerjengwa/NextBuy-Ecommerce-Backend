package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GiftCardRedeemRequestDto {
    @NotBlank(message = "Gift card code is required")
    private String code;
}
