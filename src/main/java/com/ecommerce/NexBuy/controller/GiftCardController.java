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

@RestController
@RequestMapping("/api/gift-cards")
public class GiftCardController {

    private final GiftCardService giftCardService;

    public GiftCardController(GiftCardService giftCardService) {
        this.giftCardService = giftCardService;
    }

    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GiftCardResponseDto> purchaseGiftCard(@Valid @RequestBody GiftCardPurchaseRequestDto request,
                                                                  Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        GiftCardResponseDto response = giftCardService.purchaseGiftCard(userDetails.getEmail(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/redeem")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> redeemGiftCard(@Valid @RequestBody GiftCardRedeemRequestDto request,
                                                              Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        MessageResponseDto response = giftCardService.redeemGiftCard(userDetails.getEmail(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<GiftCardResponseDto> checkGiftCard(@PathVariable String code) {
        GiftCardResponseDto response = giftCardService.getGiftCardByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-cards")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<GiftCardResponseDto>> getMyGiftCards(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<GiftCardResponseDto> cards = giftCardService.getMyGiftCards(userDetails.getEmail());
        return ResponseEntity.ok(cards);
    }
}
