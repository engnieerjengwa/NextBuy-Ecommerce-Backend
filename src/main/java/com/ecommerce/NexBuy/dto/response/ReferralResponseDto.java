package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReferralResponseDto {
    private String referralCode;
    private String shareUrl;
    private int totalReferrals;
    private int completedReferrals;
    private BigDecimal totalEarned;
    private List<ReferralDetailDto> referrals;

    @Data
    public static class ReferralDetailDto {
        private String status;
        private BigDecimal rewardAmount;
        private LocalDateTime dateCreated;
        private LocalDateTime dateCompleted;
    }
}
