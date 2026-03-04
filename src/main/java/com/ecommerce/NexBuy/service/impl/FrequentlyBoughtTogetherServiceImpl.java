package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.FrequentlyBoughtTogetherResponseDto;
import com.ecommerce.NexBuy.entity.FrequentlyBoughtTogether;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.FrequentlyBoughtTogetherRepository;
import com.ecommerce.NexBuy.service.FrequentlyBoughtTogetherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FrequentlyBoughtTogetherServiceImpl implements FrequentlyBoughtTogetherService {

    private final FrequentlyBoughtTogetherRepository frequentlyBoughtTogetherRepository;

    public FrequentlyBoughtTogetherServiceImpl(FrequentlyBoughtTogetherRepository frequentlyBoughtTogetherRepository) {
        this.frequentlyBoughtTogetherRepository = frequentlyBoughtTogetherRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FrequentlyBoughtTogetherResponseDto> getFrequentlyBoughtTogether(Long productId) {
        List<FrequentlyBoughtTogether> associations = frequentlyBoughtTogetherRepository
                .findByProductIdOrderByCoPurchaseCountDesc(productId);

        return associations.stream()
                .limit(6)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private FrequentlyBoughtTogetherResponseDto mapToResponseDto(FrequentlyBoughtTogether fbt) {
        Product relatedProduct = fbt.getRelatedProduct();
        FrequentlyBoughtTogetherResponseDto dto = new FrequentlyBoughtTogetherResponseDto();
        dto.setProductId(relatedProduct.getId());
        dto.setProductName(relatedProduct.getName());
        dto.setProductImageUrl(relatedProduct.getImageUrl());
        dto.setUnitPrice(relatedProduct.getUnitPrice());
        dto.setDiscountPercentage(fbt.getDiscountPercentage());
        dto.setCoPurchaseCount(fbt.getCoPurchaseCount());

        if (fbt.getDiscountPercentage() != null && fbt.getDiscountPercentage() > 0) {
            BigDecimal discount = relatedProduct.getUnitPrice()
                    .multiply(BigDecimal.valueOf(fbt.getDiscountPercentage()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            dto.setBundlePrice(relatedProduct.getUnitPrice().subtract(discount));
        } else {
            dto.setBundlePrice(relatedProduct.getUnitPrice());
        }

        return dto;
    }
}
