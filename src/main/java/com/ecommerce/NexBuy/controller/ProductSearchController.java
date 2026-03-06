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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "Product search, filtering, and catalog browsing")
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

    @Operation(summary = "Search products", description = "Full-text search with filters for brand, category, price range, rating, and stock")
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

    @Operation(summary = "Autocomplete product search", description = "Returns quick suggestions as the user types")
    @GetMapping("/autocomplete")
    public ResponseEntity<List<ProductSearchDto>> autocomplete(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(productSearchService.autocomplete(q, limit));
    }

    @Operation(summary = "Get available brands", description = "List all brands, optionally filtered by category")
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands(
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(productSearchService.getAvailableBrands(categoryId));
    }

    @Operation(summary = "Get product images", description = "Retrieve all images for a product ordered by display order")
    @GetMapping("/{id}/images")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@PathVariable Long id) {
        List<ProductImage> images = productImageRepository.findByProductIdOrderByDisplayOrderAsc(id);
        List<ProductImageDto> dtos = images.stream()
                .map(img -> new ProductImageDto(img.getId(), img.getImageUrl(), img.getAltText(),
                        img.getDisplayOrder(), img.getIsPrimary()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get product variants", description = "Retrieve active variants (size, color, etc.) for a product")
    @GetMapping("/{id}/variants")
    public ResponseEntity<List<ProductVariantDto>> getProductVariants(@PathVariable Long id) {
        List<ProductVariant> variants = productVariantRepository.findByProductIdAndIsActiveTrue(id);
        List<ProductVariantDto> dtos = variants.stream()
                .map(v -> new ProductVariantDto(v.getId(), v.getVariantType(), v.getVariantValue(),
                        v.getSkuSuffix(), v.getPriceAdjustment(), v.getUnitsInStock(), v.getImageUrl()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get featured products", description = "Retrieve top-rated in-stock products")
    @GetMapping("/featured")
    public ResponseEntity<Page<ProductSearchDto>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        return ResponseEntity.ok(productSearchService.searchProducts(
                null, null, null, null, null, true, new BigDecimal("4.0"), null, "rating", page, size));
    }

    @Operation(summary = "Get new arrivals", description = "Retrieve recently added products")
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
