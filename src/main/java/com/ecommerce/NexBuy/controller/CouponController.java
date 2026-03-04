package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.CouponApplyRequestDto;
import com.ecommerce.NexBuy.dto.request.CouponValidateRequestDto;
import com.ecommerce.NexBuy.dto.response.CouponResponseDto;
import com.ecommerce.NexBuy.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/validate")
    public ResponseEntity<CouponResponseDto> validateCoupon(
            @Valid @RequestBody CouponValidateRequestDto requestDto) {
        CouponResponseDto response = couponService.validateCoupon(requestDto.getCode());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CouponResponseDto> applyCoupon(
            @Valid @RequestBody CouponApplyRequestDto requestDto) {
        CouponResponseDto response = couponService.applyCoupon(requestDto);
        return ResponseEntity.ok(response);
    }
}
