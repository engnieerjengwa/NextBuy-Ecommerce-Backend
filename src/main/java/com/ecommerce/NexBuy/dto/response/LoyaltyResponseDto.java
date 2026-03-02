package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoyaltyResponseDto {
    private String tier;
    private int totalPoints;
    private int lifetimePoints;
    private LocalDate tierExpiryDate;
    private LocalDateTime dateJoined;
    private String nextTier;
    private int pointsToNextTier;
    private double progressPercentage;
    private List<LoyaltyTransactionDto> recentTransactions;
    private List<TierBenefit> tierBenefits;

    @Data
    public static class LoyaltyTransactionDto {
        private Long id;
        private int points;
        private String type;
        private String source;
        private Long orderId;
        private LocalDateTime dateCreated;
    }

    @Data
    public static class TierBenefit {
        private String tier;
        private int requiredPoints;
        private String description;
    }
}
