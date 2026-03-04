package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.LoyaltyResponseDto;

public interface LoyaltyService {

    LoyaltyResponseDto getLoyaltyStatus(String customerEmail);

    LoyaltyResponseDto earnPoints(String customerEmail, int points, String source, Long orderId);

    LoyaltyResponseDto redeemPoints(String customerEmail, int points);
}
