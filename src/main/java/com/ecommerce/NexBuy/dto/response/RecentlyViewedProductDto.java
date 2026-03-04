package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecentlyViewedProductDto {
    private Long productId;
    private String name;
    private String imageUrl;
    private BigDecimal unitPrice;
    private BigDecimal originalPrice;
    private String brand;
    private BigDecimal averageRating;
    private LocalDateTime viewedAt;
}
