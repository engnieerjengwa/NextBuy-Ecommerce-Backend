package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductAnswerRequestDto {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Answer is required")
    @Size(max = 2000, message = "Answer must be at most 2000 characters")
    private String answer;
}
