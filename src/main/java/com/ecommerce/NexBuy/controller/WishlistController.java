package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.WishlistResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@PreAuthorize("isAuthenticated()")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Get user's wishlist (auto-creates default if none exists)
     */
    @GetMapping
    public ResponseEntity<WishlistResponseDto> getWishlist(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        WishlistResponseDto wishlist = wishlistService.getWishlist(userDetails.getEmail());
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Add a product to the wishlist
     */
    @PostMapping("/items/{productId}")
    public ResponseEntity<WishlistResponseDto> addToWishlist(
            @PathVariable Long productId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        WishlistResponseDto wishlist = wishlistService.addToWishlist(userDetails.getEmail(), productId);
        return new ResponseEntity<>(wishlist, HttpStatus.CREATED);
    }

    /**
     * Remove a product from the wishlist
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long productId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        wishlistService.removeFromWishlist(userDetails.getEmail(), productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if a product is in the user's wishlist
     */
    @GetMapping("/items/{productId}/check")
    public ResponseEntity<Map<String, Boolean>> isInWishlist(
            @PathVariable Long productId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boolean inWishlist = wishlistService.isInWishlist(userDetails.getEmail(), productId);
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }
}
