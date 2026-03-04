package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.ReferralResponseDto;

public interface ReferralService {

    ReferralResponseDto getReferralInfo(String customerEmail);

    ReferralResponseDto applyReferralCode(String customerEmail, String referralCode);
}
