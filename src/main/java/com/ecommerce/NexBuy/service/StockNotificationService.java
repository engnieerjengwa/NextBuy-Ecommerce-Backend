package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.StockNotificationRequestDto;

public interface StockNotificationService {

    void subscribe(StockNotificationRequestDto requestDto);

    boolean isSubscribed(Long productId, String email);

    void unsubscribe(Long productId, String email);

    void notifySubscribers(Long productId);
}
