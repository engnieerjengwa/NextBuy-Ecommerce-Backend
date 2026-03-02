package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.WishlistItemResponseDto;
import com.ecommerce.NexBuy.dto.response.WishlistResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.Wishlist;
import com.ecommerce.NexBuy.entity.WishlistItem;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.repo.WishlistItemRepository;
import com.ecommerce.NexBuy.repo.WishlistRepository;
import com.ecommerce.NexBuy.service.WishlistService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               WishlistItemRepository wishlistItemRepository,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistResponseDto getWishlist(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);
        Wishlist wishlist = getOrCreateWishlist(customer);
        return mapToResponseDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistResponseDto addToWishlist(String customerEmail, Long productId) {
        Customer customer = findCustomerByEmail(customerEmail);
        Wishlist wishlist = getOrCreateWishlist(customer);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), productId)) {
            throw new IllegalArgumentException("Product is already in your wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        wishlistItemRepository.save(item);

        // Refresh
        Wishlist updated = wishlistRepository.findById(wishlist.getId()).orElse(wishlist);
        logger.info("Product {} added to wishlist for customer {}", productId, customerEmail);
        return mapToResponseDto(updated);
    }

    @Override
    @Transactional
    public void removeFromWishlist(String customerEmail, Long productId) {
        Customer customer = findCustomerByEmail(customerEmail);
        Wishlist wishlist = getOrCreateWishlist(customer);
        wishlistItemRepository.deleteByWishlistIdAndProductId(wishlist.getId(), productId);
        logger.info("Product {} removed from wishlist for customer {}", productId, customerEmail);
    }

    @Override
    public boolean isInWishlist(String customerEmail, Long productId) {
        Customer customer = findCustomerByEmail(customerEmail);
        return wishlistRepository.findFirstByCustomerId(customer.getId())
                .map(w -> wishlistItemRepository.existsByWishlistIdAndProductId(w.getId(), productId))
                .orElse(false);
    }

    private Customer findCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + email);
        }
        return customer;
    }

    private Wishlist getOrCreateWishlist(Customer customer) {
        return wishlistRepository.findFirstByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setCustomer(customer);
                    wishlist.setName("My Wishlist");
                    return wishlistRepository.save(wishlist);
                });
    }

    private WishlistResponseDto mapToResponseDto(Wishlist wishlist) {
        WishlistResponseDto dto = new WishlistResponseDto();
        dto.setId(wishlist.getId());
        dto.setName(wishlist.getName());
        dto.setDateCreated(wishlist.getDateCreated());
        dto.setItems(wishlist.getItems().stream()
                .map(this::mapItemToResponseDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private WishlistItemResponseDto mapItemToResponseDto(WishlistItem item) {
        WishlistItemResponseDto dto = new WishlistItemResponseDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImageUrl(item.getProduct().getImageUrl());
        dto.setProductPrice(item.getProduct().getUnitPrice());
        dto.setProductOriginalPrice(item.getProduct().getOriginalPrice());
        dto.setProductDiscountPercentage(item.getProduct().getDiscountPercentage());
        dto.setUnitsInStock(item.getProduct().getUnitsInStock());
        dto.setDateAdded(item.getDateAdded());
        return dto;
    }
}
