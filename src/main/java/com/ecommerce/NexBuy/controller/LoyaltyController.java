package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.LoyaltyRedeemRequestDto;
import com.ecommerce.NexBuy.dto.response.LoyaltyResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.LoyaltyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LoyaltyResponseDto> getLoyaltyStatus(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoyaltyResponseDto status = loyaltyService.getLoyaltyStatus(userDetails.getEmail());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/redeem")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LoyaltyResponseDto> redeemPoints(
            Authentication authentication,
            @Valid @RequestBody LoyaltyRedeemRequestDto request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoyaltyResponseDto result = loyaltyService.redeemPoints(userDetails.getEmail(), request.getPoints());
        return ResponseEntity.ok(result);
    }
}
