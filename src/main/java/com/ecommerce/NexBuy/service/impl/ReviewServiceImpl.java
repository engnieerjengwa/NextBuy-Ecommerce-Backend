package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.ReviewRequestDto;
import com.ecommerce.NexBuy.dto.response.RatingDistributionResponseDto;
import com.ecommerce.NexBuy.dto.response.ReviewResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.entity.Review;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.OrderRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.repo.ReviewRepository;
import com.ecommerce.NexBuy.service.ReviewService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             CustomerRepository customerRepository,
                             OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<ReviewResponseDto> getReviewsByProductId(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public Page<ReviewResponseDto> getReviewsByProductIdAndRating(Long productId, Integer rating, Pageable pageable) {
        return reviewRepository.findByProductIdAndRating(productId, rating, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public Page<ReviewResponseDto> getReviewsByCustomerId(Long customerId, Pageable pageable) {
        return reviewRepository.findByCustomerId(customerId, pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(String customerEmail, ReviewRequestDto requestDto) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + requestDto.getProductId()));

        if (reviewRepository.existsByProductIdAndCustomerId(product.getId(), customer.getId())) {
            throw new IllegalArgumentException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setRating(requestDto.getRating());
        review.setTitle(requestDto.getTitle());
        review.setComment(requestDto.getComment());
        review.setIsVerifiedPurchase(hasCustomerPurchasedProduct(customer, product.getId()));

        Review savedReview = reviewRepository.save(review);
        updateProductRatingStats(product);

        logger.info("Review created for product {} by customer {}", product.getId(), customerEmail);
        return mapToResponseDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(String customerEmail, Long reviewId, ReviewRequestDto requestDto) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("You can only edit your own reviews");
        }

        review.setRating(requestDto.getRating());
        review.setTitle(requestDto.getTitle());
        review.setComment(requestDto.getComment());

        Review updatedReview = reviewRepository.save(review);
        updateProductRatingStats(review.getProduct());

        return mapToResponseDto(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(String customerEmail, Long reviewId) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        Product product = review.getProduct();
        reviewRepository.delete(review);
        updateProductRatingStats(product);
    }

    @Override
    @Transactional
    public void markReviewHelpful(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        reviewRepository.save(review);
    }

    @Override
    public RatingDistributionResponseDto getRatingDistribution(Long productId) {
        Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
        Long totalReviews = reviewRepository.countByProductId(productId);
        List<Object[]> rawDistribution = reviewRepository.findRatingDistributionByProductId(productId);

        List<RatingDistributionResponseDto.RatingCount> distribution = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            long count = 0;
            for (Object[] row : rawDistribution) {
                if (((Number) row[0]).intValue() == i) {
                    count = ((Number) row[1]).longValue();
                    break;
                }
            }
            double percentage = totalReviews > 0 ? (count * 100.0 / totalReviews) : 0;
            distribution.add(new RatingDistributionResponseDto.RatingCount(i, count, Math.round(percentage * 10.0) / 10.0));
        }

        return new RatingDistributionResponseDto(
                avgRating != null ? BigDecimal.valueOf(avgRating).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO,
                totalReviews,
                distribution
        );
    }

    private void updateProductRatingStats(Product product) {
        Double avgRating = reviewRepository.findAverageRatingByProductId(product.getId());
        Long reviewCount = reviewRepository.countByProductId(product.getId());

        product.setAverageRating(avgRating != null
                ? BigDecimal.valueOf(avgRating).setScale(1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        product.setReviewCount(reviewCount != null ? reviewCount.intValue() : 0);
        productRepository.save(product);
    }

    private boolean hasCustomerPurchasedProduct(Customer customer, Long productId) {
        if (customer.getOrders() == null) return false;
        for (Order order : customer.getOrders()) {
            Set<OrderItem> items = order.getOrderItems();
            if (items != null) {
                for (OrderItem item : items) {
                    if (productId.equals(item.getProductId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ReviewResponseDto mapToResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerFirstName(review.getCustomer().getFirstName());
        dto.setCustomerLastName(review.getCustomer().getLastName());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setIsVerifiedPurchase(review.getIsVerifiedPurchase());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setDateCreated(review.getDateCreated());
        return dto;
    }
}
