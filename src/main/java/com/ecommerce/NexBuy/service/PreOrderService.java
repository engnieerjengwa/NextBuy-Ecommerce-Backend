package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PreOrderService {

    Page<Map<String, Object>> getPreOrderProducts(Pageable pageable);

    Map<String, Object> getPreOrderStatus(Long productId);

    MessageResponseDto placePreOrder(String customerEmail, Long productId, int quantity);
}
