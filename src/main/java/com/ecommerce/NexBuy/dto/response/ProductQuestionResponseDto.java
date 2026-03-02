package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductQuestionResponseDto {

    private Long id;
    private Long productId;
    private Long customerId;
    private String customerFirstName;
    private String question;
    private Boolean isAnswered;
    private LocalDateTime dateCreated;
    private List<ProductAnswerResponseDto> answers;
}
