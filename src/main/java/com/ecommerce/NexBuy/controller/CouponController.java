package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.CouponApplyRequestDto;
import com.ecommerce.NexBuy.dto.request.CouponValidateRequestDto;
import com.ecommerce.NexBuy.dto.response.CouponResponseDto;
import com.ecommerce.NexBuy.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Coupons", description = "Coupon validation and application")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "Validate coupon", description = "Check if a coupon code is valid and return its details")
    @PostMapping("/validate")
    public ResponseEntity<CouponResponseDto> validateCoupon(
            @Valid @RequestBody CouponValidateRequestDto requestDto) {
        CouponResponseDto response = couponService.validateCoupon(requestDto.getCode());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Apply coupon", description = "Apply a coupon code to the current cart")
    @PostMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CouponResponseDto> applyCoupon(
            @Valid @RequestBody CouponApplyRequestDto requestDto) {
        CouponResponseDto response = couponService.applyCoupon(requestDto);
        return ResponseEntity.ok(response);
    }
}
