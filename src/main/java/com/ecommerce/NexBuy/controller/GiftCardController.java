package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.GiftCardPurchaseRequestDto;
import com.ecommerce.NexBuy.dto.request.GiftCardRedeemRequestDto;
import com.ecommerce.NexBuy.dto.response.GiftCardResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.GiftCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Gift Cards", description = "Gift card purchase, redemption, and balance checking")
@RestController
@RequestMapping("/api/gift-cards")
public class GiftCardController {

    private final GiftCardService giftCardService;

    public GiftCardController(GiftCardService giftCardService) {
        this.giftCardService = giftCardService;
    }

    @Operation(summary = "Purchase gift card", description = "Purchase a new gift card")
    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GiftCardResponseDto> purchaseGiftCard(@Valid @RequestBody GiftCardPurchaseRequestDto request,
                                                                  Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        GiftCardResponseDto response = giftCardService.purchaseGiftCard(userDetails.getEmail(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Redeem gift card", description = "Redeem a gift card code to add credit to the wallet")
    @PostMapping("/redeem")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> redeemGiftCard(@Valid @RequestBody GiftCardRedeemRequestDto request,
                                                              Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        MessageResponseDto response = giftCardService.redeemGiftCard(userDetails.getEmail(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check gift card balance", description = "Look up a gift card's remaining balance by code")
    @GetMapping("/check/{code}")
    public ResponseEntity<GiftCardResponseDto> checkGiftCard(@PathVariable String code) {
        GiftCardResponseDto response = giftCardService.getGiftCardByCode(code);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get my gift cards", description = "Retrieve all gift cards purchased by the authenticated user")
    @GetMapping("/my-cards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GiftCardResponseDto>> getMyGiftCards(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<GiftCardResponseDto> cards = giftCardService.getMyGiftCards(userDetails.getEmail());
        return ResponseEntity.ok(cards);
    }
}
