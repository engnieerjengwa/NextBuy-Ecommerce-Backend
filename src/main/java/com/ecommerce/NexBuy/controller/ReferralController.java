package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.ReferralApplyRequestDto;
import com.ecommerce.NexBuy.dto.response.ReferralResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.ReferralService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Referrals", description = "Referral program and invite code management")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @Operation(summary = "Get referral info", description = "Retrieve the user's referral code and referral statistics")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReferralResponseDto> getReferralInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ReferralResponseDto info = referralService.getReferralInfo(userDetails.getEmail());
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Apply referral code", description = "Apply a referral code from another user to earn rewards")
    @PostMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReferralResponseDto> applyReferralCode(
            Authentication authentication,
            @Valid @RequestBody ReferralApplyRequestDto request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ReferralResponseDto result = referralService.applyReferralCode(userDetails.getEmail(), request.getReferralCode());
        return ResponseEntity.ok(result);
    }
}
