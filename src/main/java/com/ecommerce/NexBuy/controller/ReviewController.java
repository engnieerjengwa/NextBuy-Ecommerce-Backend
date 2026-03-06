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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reviews", description = "Product reviews, ratings, and seller responses")
@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Get product reviews", description = "Retrieve paginated reviews for a product with optional rating filter")
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

    @Operation(summary = "Get rating distribution", description = "Retrieve the star-rating breakdown for a product")
    @GetMapping("/products/{productId}/reviews/distribution")
    public ResponseEntity<RatingDistributionResponseDto> getRatingDistribution(
            @PathVariable Long productId) {
        RatingDistributionResponseDto distribution = reviewService.getRatingDistribution(productId);
        return ResponseEntity.ok(distribution);
    }

    @Operation(summary = "Create review", description = "Submit a new review for a product")
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

    @Operation(summary = "Update review", description = "Update an existing review owned by the authenticated user")
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

    @Operation(summary = "Delete review", description = "Delete a review owned by the authenticated user")
    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        reviewService.deleteReview(userDetails.getEmail(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark review helpful", description = "Vote a review as helpful")
    @PostMapping("/reviews/{reviewId}/helpful")
    public ResponseEntity<Void> markReviewHelpful(@PathVariable Long reviewId) {
        reviewService.markReviewHelpful(reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get my reviews", description = "Retrieve all reviews submitted by the authenticated user")
    @GetMapping("/reviews/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ReviewResponseDto>> getMyReviews(
            Authentication authentication, Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByCustomerId(userDetails.getId(), pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Respond to review", description = "Add a seller or admin response to a customer review")
    @PostMapping("/reviews/{reviewId}/respond")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDto> respondToReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewResponseRequestDto request) {
        ReviewResponseDto review = reviewService.respondToReview(reviewId, request.getResponse());
        return ResponseEntity.ok(review);
    }
}
