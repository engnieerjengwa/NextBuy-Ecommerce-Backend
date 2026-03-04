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

@RestController
@RequestMapping("/api/referrals")
public class ReferralController {

    private final ReferralService referralService;

    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReferralResponseDto> getReferralInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ReferralResponseDto info = referralService.getReferralInfo(userDetails.getEmail());
        return ResponseEntity.ok(info);
    }

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
