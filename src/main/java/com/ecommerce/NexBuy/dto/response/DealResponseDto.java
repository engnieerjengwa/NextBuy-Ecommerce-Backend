package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DealResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal originalPrice;
    private BigDecimal dealPrice;
    private Integer discountPercentage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxQuantity;
    private Integer soldQuantity;
    private String dealType;
    private String title;
    private String description;
    private long remainingSeconds;
}
