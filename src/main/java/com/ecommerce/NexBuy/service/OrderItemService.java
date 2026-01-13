package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.OrderItemRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderItemResponseDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemResponseDto> findAll();
    OrderItemResponseDto findById(Long id);
    OrderItemResponseDto save(OrderItemRequestDto orderItemRequestDto);
    OrderItemResponseDto update(Long id, OrderItemRequestDto orderItemRequestDto);
    void deleteById(Long id);
}