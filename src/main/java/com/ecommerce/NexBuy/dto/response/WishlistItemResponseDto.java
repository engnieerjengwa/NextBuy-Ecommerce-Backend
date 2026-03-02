package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WishlistItemResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
    private BigDecimal productOriginalPrice;
    private Integer productDiscountPercentage;
    private Integer unitsInStock;
    private LocalDateTime dateAdded;
}
