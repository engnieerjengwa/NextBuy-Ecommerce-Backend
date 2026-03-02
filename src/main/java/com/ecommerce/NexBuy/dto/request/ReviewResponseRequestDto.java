package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewResponseRequestDto {
    @NotBlank(message = "Response text is required")
    private String response;
}
