package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FrequentlyBoughtTogetherResponseDto {
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal unitPrice;
    private BigDecimal bundlePrice;
    private Integer discountPercentage;
    private Integer coPurchaseCount;
}
