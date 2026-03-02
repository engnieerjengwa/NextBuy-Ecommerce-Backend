package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.StockNotificationRequestDto;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.StockNotification;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.repo.StockNotificationRepository;
import com.ecommerce.NexBuy.service.EmailService;
import com.ecommerce.NexBuy.service.StockNotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockNotificationServiceImpl implements StockNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(StockNotificationServiceImpl.class);

    private final StockNotificationRepository stockNotificationRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Autowired
    public StockNotificationServiceImpl(StockNotificationRepository stockNotificationRepository,
                                        ProductRepository productRepository,
                                        EmailService emailService) {
        this.stockNotificationRepository = stockNotificationRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void subscribe(StockNotificationRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + requestDto.getProductId()));

        if (stockNotificationRepository.existsByProductIdAndCustomerEmail(product.getId(), requestDto.getCustomerEmail())) {
            throw new IllegalArgumentException("You are already subscribed to notifications for this product");
        }

        StockNotification notification = new StockNotification();
        notification.setProduct(product);
        notification.setCustomerEmail(requestDto.getCustomerEmail());
        stockNotificationRepository.save(notification);

        logger.info("Stock notification subscription created for product {} by {}", product.getId(), requestDto.getCustomerEmail());
    }

    @Override
    public boolean isSubscribed(Long productId, String email) {
        return stockNotificationRepository.existsByProductIdAndCustomerEmail(productId, email);
    }

    @Override
    @Transactional
    public void unsubscribe(Long productId, String email) {
        StockNotification notification = stockNotificationRepository
                .findByProductIdAndCustomerEmail(productId, email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No subscription found for product " + productId + " and email " + email));
        stockNotificationRepository.delete(notification);
        logger.info("Stock notification subscription removed for product {} by {}", productId, email);
    }

    @Override
    @Transactional
    public void notifySubscribers(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getUnitsInStock() <= 0) {
            logger.warn("Product {} is still out of stock", productId);
            return;
        }

        List<StockNotification> notifications = stockNotificationRepository.findByProductIdAndIsNotifiedFalse(productId);

        for (StockNotification notification : notifications) {
            try {
                emailService.sendStockNotificationEmail(
                        notification.getCustomerEmail(),
                        product.getName(),
                        product.getId()
                );
                notification.setIsNotified(true);
                notification.setDateNotified(LocalDateTime.now());
                stockNotificationRepository.save(notification);
                logger.info("Stock notification sent to {} for product {}", notification.getCustomerEmail(), productId);
            } catch (Exception e) {
                logger.error("Failed to send stock notification to {}: {}", notification.getCustomerEmail(), e.getMessage());
            }
        }
    }
}
