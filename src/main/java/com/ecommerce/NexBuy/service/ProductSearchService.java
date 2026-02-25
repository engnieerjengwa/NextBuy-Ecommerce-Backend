package com.ecommerce.NexBuy.service;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductSearchService {

    Page<ProductSearchDto> searchProducts(String query, String brand, Long categoryId,
                                           BigDecimal minPrice, BigDecimal maxPrice,
                                           Boolean inStock, BigDecimal minRating, Boolean isNew,
                                           String sort, int page, int size);

    List<ProductSearchDto> autocomplete(String query, int limit);

    List<String> getAvailableBrands(Long categoryId);

    record ProductSearchDto(
            Long id,
            String sku,
            String name,
            String description,
            BigDecimal unitPrice,
            String imageUrl,
            boolean active,
            int unitsInStock,
            BigDecimal originalPrice,
            Integer discountPercentage,
            String brand,
            Boolean isNew,
            BigDecimal averageRating,
            Integer reviewCount,
            Long categoryId,
            String categoryName
    ) {}
}
