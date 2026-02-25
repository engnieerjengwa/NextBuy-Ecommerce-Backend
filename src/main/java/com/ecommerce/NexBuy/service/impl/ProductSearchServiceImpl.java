package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.ProductSearchService;
import com.ecommerce.NexBuy.service.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductRepository productRepository;

    public ProductSearchServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductSearchDto> searchProducts(String query, String brand, Long categoryId,
                                                  BigDecimal minPrice, BigDecimal maxPrice,
                                                  Boolean inStock, BigDecimal minRating, Boolean isNew,
                                                  String sort, int page, int size) {

        Specification<Product> spec = ProductSpecification.buildSearchSpec(
                query, brand, categoryId, minPrice, maxPrice, inStock, minRating, isNew);

        Sort sorting = buildSort(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);

        return productRepository.findAll(spec, pageable).map(this::toDto);
    }

    @Override
    public List<ProductSearchDto> autocomplete(String query, int limit) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        Specification<Product> spec = ProductSpecification.buildSearchSpec(
                query, null, null, null, null, null, null, null);

        Pageable pageable = PageRequest.of(0, limit, Sort.by("name").ascending());
        return productRepository.findAll(spec, pageable)
                .getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableBrands(Long categoryId) {
        if (categoryId != null) {
            return productRepository.findDistinctBrandsByCategoryId(categoryId);
        }
        return productRepository.findDistinctBrands();
    }

    private Sort buildSort(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.unsorted();
        }
        return switch (sort.toLowerCase()) {
            case "price_asc" -> Sort.by("unitPrice").ascending();
            case "price_desc" -> Sort.by("unitPrice").descending();
            case "newest" -> Sort.by("id").descending();
            case "rating" -> Sort.by("averageRating").descending();
            case "best_selling" -> Sort.by("reviewCount").descending();
            case "name_asc" -> Sort.by("name").ascending();
            case "name_desc" -> Sort.by("name").descending();
            default -> Sort.unsorted();
        };
    }

    private ProductSearchDto toDto(Product product) {
        return new ProductSearchDto(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getImageUrl(),
                product.isActive(),
                product.getUnitsInStock(),
                product.getOriginalPrice(),
                product.getDiscountPercentage(),
                product.getBrand(),
                product.getIsNew(),
                product.getAverageRating(),
                product.getReviewCount(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getCategoryName() : null
        );
    }
}
