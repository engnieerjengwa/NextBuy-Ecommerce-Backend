package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductAnswerResponseDto {

    private Long id;
    private Long answeredByCustomerId;
    private String answeredByName;
    private Boolean answeredBySeller;
    private String answer;
    private Integer helpfulCount;
    private LocalDateTime dateCreated;
}
