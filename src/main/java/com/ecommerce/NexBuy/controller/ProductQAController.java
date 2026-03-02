package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.ProductAnswerRequestDto;
import com.ecommerce.NexBuy.dto.request.ProductQuestionRequestDto;
import com.ecommerce.NexBuy.dto.response.ProductAnswerResponseDto;
import com.ecommerce.NexBuy.dto.response.ProductQuestionResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.ProductQAService;
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
public class ProductQAController {

    private final ProductQAService productQAService;

    public ProductQAController(ProductQAService productQAService) {
        this.productQAService = productQAService;
    }

    /**
     * Get Q&A for a product (public, paginated)
     */
    @GetMapping("/products/{productId}/questions")
    public ResponseEntity<Page<ProductQuestionResponseDto>> getQuestions(
            @PathVariable Long productId, Pageable pageable) {
        Page<ProductQuestionResponseDto> questions = productQAService.getQuestionsByProductId(productId, pageable);
        return ResponseEntity.ok(questions);
    }

    /**
     * Ask a question about a product (auth required)
     */
    @PostMapping("/products/{productId}/questions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductQuestionResponseDto> askQuestion(
            @PathVariable Long productId,
            @Valid @RequestBody ProductQuestionRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        requestDto.setProductId(productId);
        ProductQuestionResponseDto question = productQAService.askQuestion(userDetails.getEmail(), requestDto);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    /**
     * Submit an answer to a question (auth required)
     */
    @PostMapping("/questions/{questionId}/answers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductAnswerResponseDto> answerQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody ProductAnswerRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        requestDto.setQuestionId(questionId);
        ProductAnswerResponseDto answer = productQAService.answerQuestion(userDetails.getEmail(), requestDto);
        return new ResponseEntity<>(answer, HttpStatus.CREATED);
    }

    /**
     * Vote an answer as helpful (public)
     */
    @PostMapping("/answers/{answerId}/helpful")
    public ResponseEntity<Void> markAnswerHelpful(@PathVariable Long answerId) {
        productQAService.markAnswerHelpful(answerId);
        return ResponseEntity.ok().build();
    }
}
