package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.DealResponseDto;

import java.util.List;

public interface DealService {

    List<DealResponseDto> getActiveDeals();

    List<DealResponseDto> getDailyDeals();

    List<DealResponseDto> getFlashSales();

    DealResponseDto getDealById(Long dealId);
}
