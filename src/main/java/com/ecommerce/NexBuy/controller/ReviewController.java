package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.ReviewRequestDto;
import com.ecommerce.NexBuy.dto.request.ReviewResponseRequestDto;
import com.ecommerce.NexBuy.dto.response.RatingDistributionResponseDto;
import com.ecommerce.NexBuy.dto.response.ReviewResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Get reviews for a product (public, paginated, optional rating filter)
     */
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByProduct(
            @PathVariable Long productId,
            @RequestParam(required = false) Integer rating,
            Pageable pageable) {
        Page<ReviewResponseDto> reviews;
        if (rating != null) {
            reviews = reviewService.getReviewsByProductIdAndRating(productId, rating, pageable);
        } else {
            reviews = reviewService.getReviewsByProductId(productId, pageable);
        }
        return ResponseEntity.ok(reviews);
    }

    /**
     * Get rating distribution for a product (public)
     */
    @GetMapping("/products/{productId}/reviews/distribution")
    public ResponseEntity<RatingDistributionResponseDto> getRatingDistribution(
            @PathVariable Long productId) {
        RatingDistributionResponseDto distribution = reviewService.getRatingDistribution(productId);
        return ResponseEntity.ok(distribution);
    }

    /**
     * Submit a review for a product (auth required)
     */
    @PostMapping("/products/{productId}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        reviewRequestDto.setProductId(productId);
        ReviewResponseDto review = reviewService.createReview(userDetails.getEmail(), reviewRequestDto);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    /**
     * Update own review (auth required)
     */
    @PutMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ReviewResponseDto review = reviewService.updateReview(userDetails.getEmail(), reviewId, reviewRequestDto);
        return ResponseEntity.ok(review);
    }

    /**
     * Delete own review (auth required)
     */
    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        reviewService.deleteReview(userDetails.getEmail(), reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vote a review as helpful (public or auth - no restriction)
     */
    @PostMapping("/reviews/{reviewId}/helpful")
    public ResponseEntity<Void> markReviewHelpful(@PathVariable Long reviewId) {
        reviewService.markReviewHelpful(reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get reviews by the authenticated customer
     */
    @GetMapping("/reviews/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ReviewResponseDto>> getMyReviews(
            Authentication authentication, Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByCustomerId(userDetails.getId(), pageable);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Add a seller response to a review (admin/seller only)
     */
    @PostMapping("/reviews/{reviewId}/respond")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDto> respondToReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewResponseRequestDto request) {
        ReviewResponseDto review = reviewService.respondToReview(reviewId, request.getResponse());
        return ResponseEntity.ok(review);
    }
}
