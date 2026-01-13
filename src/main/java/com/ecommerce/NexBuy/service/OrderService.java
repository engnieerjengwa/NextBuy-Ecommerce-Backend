package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.OrderRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> findAll();
    OrderResponseDto findById(Long id);
    OrderResponseDto save(OrderRequestDto orderRequestDto);
    OrderResponseDto update(Long id, OrderRequestDto orderRequestDto);
    void deleteById(Long id);
}