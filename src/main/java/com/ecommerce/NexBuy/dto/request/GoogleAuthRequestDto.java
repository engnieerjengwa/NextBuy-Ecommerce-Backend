package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleAuthRequestDto {

    @NotBlank(message = "Google credential token is required")
    private String credential;
}
