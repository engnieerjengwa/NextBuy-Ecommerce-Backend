package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductAdminService {

    Product createProduct(ProductCreateRequest request);

    Product updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);

    Product getProduct(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    record ProductCreateRequest(
            String sku,
            String name,
            String description,
            BigDecimal unitPrice,
            String imageUrl,
            boolean active,
            int unitsInStock,
            Long categoryId,
            BigDecimal originalPrice,
            Integer discountPercentage,
            String brand,
            String specifications,
            BigDecimal weightKg,
            BigDecimal lengthCm,
            BigDecimal widthCm,
            BigDecimal heightCm,
            String videoUrl,
            String warrantyInfo,
            Boolean isNew,
            List<ImageRequest> images
    ) {}

    record ProductUpdateRequest(
            String sku,
            String name,
            String description,
            BigDecimal unitPrice,
            String imageUrl,
            Boolean active,
            Integer unitsInStock,
            Long categoryId,
            BigDecimal originalPrice,
            Integer discountPercentage,
            String brand,
            String specifications,
            BigDecimal weightKg,
            BigDecimal lengthCm,
            BigDecimal widthCm,
            BigDecimal heightCm,
            String videoUrl,
            String warrantyInfo,
            Boolean isNew,
            List<ImageRequest> images
    ) {}

    record ImageRequest(
            String imageUrl,
            String altText,
            Integer displayOrder,
            Boolean isPrimary
    ) {}
}
