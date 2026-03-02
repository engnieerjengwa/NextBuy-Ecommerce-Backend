package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductQuestionRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Question is required")
    @Size(max = 2000, message = "Question must be at most 2000 characters")
    private String question;
}
