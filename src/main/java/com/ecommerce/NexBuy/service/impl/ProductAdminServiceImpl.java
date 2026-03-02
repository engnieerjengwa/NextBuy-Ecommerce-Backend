package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.ProductCategory;
import com.ecommerce.NexBuy.entity.ProductImage;
import com.ecommerce.NexBuy.repo.ProductCategoryRepository;
import com.ecommerce.NexBuy.repo.ProductImageRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.ProductAdminService;
import com.ecommerce.NexBuy.service.StockNotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAdminServiceImpl implements ProductAdminService {

    private static final Logger logger = LoggerFactory.getLogger(ProductAdminServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final StockNotificationService stockNotificationService;

    public ProductAdminServiceImpl(ProductRepository productRepository,
                                    ProductCategoryRepository productCategoryRepository,
                                    ProductImageRepository productImageRepository,
                                    StockNotificationService stockNotificationService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productImageRepository = productImageRepository;
        this.stockNotificationService = stockNotificationService;
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        ProductCategory category = productCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.categoryId()));

        Product product = new Product();
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setUnitPrice(request.unitPrice());
        product.setImageUrl(request.imageUrl());
        product.setActive(request.active());
        product.setUnitsInStock(request.unitsInStock());
        product.setCategory(category);
        product.setOriginalPrice(request.originalPrice());
        product.setDiscountPercentage(request.discountPercentage());
        product.setBrand(request.brand());
        product.setSpecifications(request.specifications());
        product.setWeightKg(request.weightKg());
        product.setLengthCm(request.lengthCm());
        product.setWidthCm(request.widthCm());
        product.setHeightCm(request.heightCm());
        product.setVideoUrl(request.videoUrl());
        product.setWarrantyInfo(request.warrantyInfo());
        product.setIsNew(request.isNew() != null ? request.isNew() : false);

        Product savedProduct = productRepository.save(product);

        // Add images if provided
        if (request.images() != null && !request.images().isEmpty()) {
            for (ImageRequest imgReq : request.images()) {
                ProductImage image = new ProductImage();
                image.setProduct(savedProduct);
                image.setImageUrl(imgReq.imageUrl());
                image.setAltText(imgReq.altText());
                image.setDisplayOrder(imgReq.displayOrder() != null ? imgReq.displayOrder() : 0);
                image.setIsPrimary(imgReq.isPrimary() != null ? imgReq.isPrimary() : false);
                productImageRepository.save(image);
            }
        }

        return savedProduct;
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        int oldStock = product.getUnitsInStock();

        if (request.name() != null) product.setName(request.name());
        if (request.sku() != null) product.setSku(request.sku());
        if (request.description() != null) product.setDescription(request.description());
        if (request.unitPrice() != null) product.setUnitPrice(request.unitPrice());
        if (request.imageUrl() != null) product.setImageUrl(request.imageUrl());
        if (request.active() != null) product.setActive(request.active());
        if (request.unitsInStock() != null) product.setUnitsInStock(request.unitsInStock());
        if (request.originalPrice() != null) product.setOriginalPrice(request.originalPrice());
        if (request.discountPercentage() != null) product.setDiscountPercentage(request.discountPercentage());
        if (request.brand() != null) product.setBrand(request.brand());
        if (request.specifications() != null) product.setSpecifications(request.specifications());
        if (request.weightKg() != null) product.setWeightKg(request.weightKg());
        if (request.lengthCm() != null) product.setLengthCm(request.lengthCm());
        if (request.widthCm() != null) product.setWidthCm(request.widthCm());
        if (request.heightCm() != null) product.setHeightCm(request.heightCm());
        if (request.videoUrl() != null) product.setVideoUrl(request.videoUrl());
        if (request.warrantyInfo() != null) product.setWarrantyInfo(request.warrantyInfo());
        if (request.isNew() != null) product.setIsNew(request.isNew());

        if (request.categoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.categoryId()));
            product.setCategory(category);
        }

        // Replace images if provided
        if (request.images() != null) {
            // Remove existing images
            List<ProductImage> existingImages = productImageRepository.findByProductIdOrderByDisplayOrderAsc(id);
            productImageRepository.deleteAll(existingImages);

            // Add new images
            for (ImageRequest imgReq : request.images()) {
                ProductImage image = new ProductImage();
                image.setProduct(product);
                image.setImageUrl(imgReq.imageUrl());
                image.setAltText(imgReq.altText());
                image.setDisplayOrder(imgReq.displayOrder() != null ? imgReq.displayOrder() : 0);
                image.setIsPrimary(imgReq.isPrimary() != null ? imgReq.isPrimary() : false);
                productImageRepository.save(image);
            }
        }

        Product savedProduct = productRepository.save(product);

        // Auto-trigger back-in-stock notifications when stock goes from 0 to positive
        int newStock = savedProduct.getUnitsInStock();
        if (oldStock <= 0 && newStock > 0) {
            logger.info("Product {} restocked (0 -> {}), triggering back-in-stock notifications", id, newStock);
            stockNotificationService.notifySubscribers(id);
        }

        return savedProduct;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
        // Soft delete — deactivate instead of hard delete
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
