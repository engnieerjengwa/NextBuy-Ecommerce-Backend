package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.PreOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PreOrderServiceImpl.class);

    private final ProductRepository productRepository;

    public PreOrderServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getPreOrderProducts(Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> cb.equal(root.get("isPreorder"), true);

        Page<Product> products = productRepository.findAll(spec, pageable);
        return products.map(this::mapToPreOrderInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPreOrderStatus(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (product.getIsPreorder() == null || !product.getIsPreorder()) {
            throw new IllegalArgumentException("Product is not available for pre-order");
        }

        return mapToPreOrderInfo(product);
    }

    @Override
    @Transactional
    public MessageResponseDto placePreOrder(String customerEmail, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (product.getIsPreorder() == null || !product.getIsPreorder()) {
            throw new IllegalArgumentException("Product is not available for pre-order");
        }

        if (product.getPreorderReleaseDate() != null && product.getPreorderReleaseDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Pre-order period has ended. Product is now available for regular purchase.");
        }

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        logger.info("Pre-order placed: customer={}, productId={}, quantity={}", customerEmail, productId, quantity);

        String message = String.format("Pre-order placed successfully for '%s'. Expected release: %s",
                product.getName(),
                product.getPreorderReleaseDate() != null ? product.getPreorderReleaseDate().toString() : "TBA");

        return new MessageResponseDto(message);
    }

    private Map<String, Object> mapToPreOrderInfo(Product product) {
        Map<String, Object> info = new HashMap<>();
        info.put("productId", product.getId());
        info.put("name", product.getName());
        info.put("unitPrice", product.getUnitPrice());
        info.put("imageUrl", product.getImageUrl());
        info.put("isPreorder", product.getIsPreorder());
        info.put("preorderReleaseDate", product.getPreorderReleaseDate());
        info.put("preorderMessage", product.getPreorderMessage());
        info.put("available", product.getPreorderReleaseDate() == null || !product.getPreorderReleaseDate().isBefore(LocalDate.now()));
        return info;
    }
}
