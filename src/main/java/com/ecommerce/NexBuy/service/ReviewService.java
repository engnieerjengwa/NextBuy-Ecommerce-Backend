package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.ReviewRequestDto;
import com.ecommerce.NexBuy.dto.response.RatingDistributionResponseDto;
import com.ecommerce.NexBuy.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    Page<ReviewResponseDto> getReviewsByProductId(Long productId, Pageable pageable);

    Page<ReviewResponseDto> getReviewsByProductIdAndRating(Long productId, Integer rating, Pageable pageable);

    Page<ReviewResponseDto> getReviewsByCustomerId(Long customerId, Pageable pageable);

    ReviewResponseDto createReview(String customerEmail, ReviewRequestDto reviewRequestDto);

    ReviewResponseDto updateReview(String customerEmail, Long reviewId, ReviewRequestDto reviewRequestDto);

    void deleteReview(String customerEmail, Long reviewId);

    void markReviewHelpful(Long reviewId);

    RatingDistributionResponseDto getRatingDistribution(Long productId);
}
