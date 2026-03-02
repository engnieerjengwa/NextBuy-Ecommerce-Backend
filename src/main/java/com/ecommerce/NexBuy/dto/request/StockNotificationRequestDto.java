package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockNotificationRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String customerEmail;
}
