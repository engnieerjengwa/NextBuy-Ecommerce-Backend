package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.FrequentlyBoughtTogetherResponseDto;

import java.util.List;

public interface FrequentlyBoughtTogetherService {

    List<FrequentlyBoughtTogetherResponseDto> getFrequentlyBoughtTogether(Long productId);
}
