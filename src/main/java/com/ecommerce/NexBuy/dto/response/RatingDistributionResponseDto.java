package com.ecommerce.NexBuy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDistributionResponseDto {

    private BigDecimal averageRating;
    private Long totalReviews;
    private List<RatingCount> distribution;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingCount {
        private Integer rating;
        private Long count;
        private Double percentage;
    }
}
