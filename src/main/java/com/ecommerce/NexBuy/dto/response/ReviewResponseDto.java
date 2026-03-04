package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private Integer rating;
    private String title;
    private String comment;
    private Boolean isVerifiedPurchase;
    private Integer helpfulCount;
    private LocalDateTime dateCreated;
    private String sellerResponse;
    private LocalDateTime sellerResponseDate;
}
