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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Loyalty", description = "Loyalty rewards program and points redemption")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @Operation(summary = "Get loyalty status", description = "Retrieve the loyalty points balance and tier for the authenticated user")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LoyaltyResponseDto> getLoyaltyStatus(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoyaltyResponseDto status = loyaltyService.getLoyaltyStatus(userDetails.getEmail());
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Redeem loyalty points", description = "Redeem accumulated loyalty points for store credit")
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
