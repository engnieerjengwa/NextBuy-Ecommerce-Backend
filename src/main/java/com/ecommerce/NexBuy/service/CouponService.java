package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.CouponApplyRequestDto;
import com.ecommerce.NexBuy.dto.response.CouponResponseDto;

public interface CouponService {

    CouponResponseDto validateCoupon(String code);

    CouponResponseDto applyCoupon(CouponApplyRequestDto requestDto);
}
