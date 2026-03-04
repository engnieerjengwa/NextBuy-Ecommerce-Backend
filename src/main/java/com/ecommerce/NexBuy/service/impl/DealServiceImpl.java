package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.DealResponseDto;
import com.ecommerce.NexBuy.entity.Deal;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.DealRepository;
import com.ecommerce.NexBuy.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealServiceImpl implements DealService {

    private static final Logger logger = LoggerFactory.getLogger(DealServiceImpl.class);

    private final DealRepository dealRepository;

    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public List<DealResponseDto> getActiveDeals() {
        List<Deal> deals = dealRepository.findActiveDeals(LocalDateTime.now());
        return deals.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<DealResponseDto> getDailyDeals() {
        List<Deal> deals = dealRepository.findActiveDailyDeals(LocalDateTime.now());
        return deals.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<DealResponseDto> getFlashSales() {
        List<Deal> deals = dealRepository.findActiveFlashSales(LocalDateTime.now());
        return deals.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    public DealResponseDto getDealById(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("Deal not found with ID: " + dealId));
        return mapToResponseDto(deal);
    }

    private DealResponseDto mapToResponseDto(Deal deal) {
        Product product = deal.getProduct();
        DealResponseDto dto = new DealResponseDto();
        dto.setId(deal.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductImageUrl(product.getImageUrl());
        dto.setOriginalPrice(product.getUnitPrice());
        dto.setDealPrice(deal.getDealPrice());

        if (product.getUnitPrice() != null && product.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = product.getUnitPrice().subtract(deal.getDealPrice())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(product.getUnitPrice(), 0, RoundingMode.HALF_UP);
            dto.setDiscountPercentage(discount.intValue());
        } else {
            dto.setDiscountPercentage(0);
        }

        dto.setStartTime(deal.getStartTime());
        dto.setEndTime(deal.getEndTime());
        dto.setMaxQuantity(deal.getMaxQuantity());
        dto.setSoldQuantity(deal.getSoldQuantity());
        dto.setDealType(deal.getDealType().name());
        dto.setTitle(deal.getTitle());
        dto.setDescription(deal.getDescription());

        long remainingSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deal.getEndTime());
        dto.setRemainingSeconds(Math.max(0, remainingSeconds));

        return dto;
    }
}
