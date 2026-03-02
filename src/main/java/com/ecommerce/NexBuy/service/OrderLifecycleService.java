package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.OrderStatusUpdateRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderResponseDto;
import com.ecommerce.NexBuy.dto.response.OrderStatusHistoryResponseDto;

import java.util.List;

public interface OrderLifecycleService {

    OrderResponseDto updateOrderStatus(OrderStatusUpdateRequestDto requestDto);

    List<OrderStatusHistoryResponseDto> getOrderStatusHistory(Long orderId);

    OrderResponseDto cancelOrder(String customerEmail, Long orderId);

    OrderResponseDto reorderFromPrevious(String customerEmail, Long orderId);
}
