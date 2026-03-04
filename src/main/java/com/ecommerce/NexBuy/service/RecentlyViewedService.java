package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.RecentlyViewedProductDto;

import java.util.List;

public interface RecentlyViewedService {

    /**
     * Track a product view for a customer.
     */
    void trackProductView(String customerEmail, Long productId);

    /**
     * Get recently viewed products for a customer (max 20).
     */
    List<RecentlyViewedProductDto> getRecentlyViewedProducts(String customerEmail);
}
