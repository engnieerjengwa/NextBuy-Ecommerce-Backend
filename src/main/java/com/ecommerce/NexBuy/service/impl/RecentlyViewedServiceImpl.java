package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.RecentlyViewedProductDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.RecentlyViewed;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.repo.RecentlyViewedRepository;
import com.ecommerce.NexBuy.service.RecentlyViewedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecentlyViewedServiceImpl implements RecentlyViewedService {

    private static final Logger logger = LoggerFactory.getLogger(RecentlyViewedServiceImpl.class);

    private final RecentlyViewedRepository recentlyViewedRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public RecentlyViewedServiceImpl(RecentlyViewedRepository recentlyViewedRepository,
                                     CustomerRepository customerRepository,
                                     ProductRepository productRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void trackProductView(String customerEmail, Long productId) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Optional<RecentlyViewed> existing = recentlyViewedRepository.findByCustomerIdAndProductId(customer.getId(), productId);

        if (existing.isPresent()) {
            existing.get().setViewedAt(LocalDateTime.now());
            recentlyViewedRepository.save(existing.get());
        } else {
            RecentlyViewed recentlyViewed = new RecentlyViewed();
            recentlyViewed.setCustomer(customer);
            recentlyViewed.setProduct(product);
            recentlyViewed.setViewedAt(LocalDateTime.now());
            recentlyViewedRepository.save(recentlyViewed);

            // Clean up old entries beyond 20
            recentlyViewedRepository.keepOnlyLatest20(customer.getId());
        }

        logger.debug("Tracked product view: customer={}, product={}", customerEmail, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentlyViewedProductDto> getRecentlyViewedProducts(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        List<RecentlyViewed> recentlyViewed = recentlyViewedRepository.findTop20ByCustomerIdOrderByViewedAtDesc(customer.getId());

        return recentlyViewed.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RecentlyViewedProductDto mapToDto(RecentlyViewed rv) {
        RecentlyViewedProductDto dto = new RecentlyViewedProductDto();
        Product product = rv.getProduct();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setBrand(product.getBrand());
        dto.setAverageRating(product.getAverageRating());
        dto.setViewedAt(rv.getViewedAt());
        return dto;
    }
}
