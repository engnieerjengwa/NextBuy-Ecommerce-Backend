package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.WishlistResponseDto;

public interface WishlistService {

    WishlistResponseDto getWishlist(String customerEmail);

    WishlistResponseDto addToWishlist(String customerEmail, Long productId);

    void removeFromWishlist(String customerEmail, Long productId);

    boolean isInWishlist(String customerEmail, Long productId);
}
