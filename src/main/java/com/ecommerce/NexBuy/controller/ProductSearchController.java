package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.repo.ProductImageRepository;
import com.ecommerce.NexBuy.repo.ProductVariantRepository;
import com.ecommerce.NexBuy.entity.ProductImage;
import com.ecommerce.NexBuy.entity.ProductVariant;
import com.ecommerce.NexBuy.service.ProductSearchService;
import com.ecommerce.NexBuy.service.ProductSearchService.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductSearchController {

    private final ProductSearchService productSearchService;
    private final ProductImageRepository productImageRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductSearchController(ProductSearchService productSearchService,
                                    ProductImageRepository productImageRepository,
                                    ProductVariantRepository productVariantRepository) {
        this.productSearchService = productSearchService;
        this.productImageRepository = productImageRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductSearchDto>> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) Boolean isNew,
            @RequestParam(defaultValue = "relevance") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ProductSearchDto> results = productSearchService.searchProducts(
                q, brand, categoryId, minPrice, maxPrice, inStock, minRating, isNew, sort, page, size);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<ProductSearchDto>> autocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(productSearchService.autocomplete(q, limit));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(productSearchService.getAvailableBrands(categoryId));
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@PathVariable Long id) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(id);
        List<ProductImageDto> dtos = images.stream()
                .map(img -> new ProductImageDto(img.getId(), img.getImageUrl(), img.getAltText(),
                        img.getDisplayOrder(), img.getIsPrimary()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/variants")
    public ResponseEntity<List<ProductVariantDto>> getProductVariants(@PathVariable Long id) {
        List<ProductVariant> variants = productVariantRepository.findByProductIdAndIsActiveTrue(id);
        List<ProductVariantDto> dtos = variants.stream()
                .map(v -> new ProductVariantDto(v.getId(), v.getVariantType(), v.getVariantValue(),
                        v.getSkuSuffix(), v.getPriceAdjustment(), v.getUnitsInStock(), v.getImageUrl()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<ProductSearchDto>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        return ResponseEntity.ok(productSearchService.searchProducts(
                null, null, null, null, null, true, new BigDecimal("4.0"), null, "rating", page, size));
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<Page<ProductSearchDto>> getNewArrivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        return ResponseEntity.ok(productSearchService.searchProducts(
                null, null, null, null, null, null, null, true, "newest", page, size));
    }

    // DTO records
    record ProductImageDto(Long id, String imageUrl, String altText, Integer displayOrder, Boolean isPrimary) {}
    record ProductVariantDto(Long id, String variantType, String variantValue, String skuSuffix,
                              BigDecimal priceAdjustment, int unitsInStock, String imageUrl) {}
}
