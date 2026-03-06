package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.WalletResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Wallet", description = "Store credit wallet balance and transactions")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Get wallet", description = "Retrieve or create the store credit wallet for the authenticated user")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WalletResponseDto> getWallet(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        WalletResponseDto wallet = walletService.getOrCreateWallet(userDetails.getEmail());
        return ResponseEntity.ok(wallet);
    }
}
