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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Q&A", description = "Product questions and community answers")
@RestController
@RequestMapping("/api")
public class ProductQAController {

    private final ProductQAService productQAService;

    public ProductQAController(ProductQAService productQAService) {
        this.productQAService = productQAService;
    }

    @Operation(summary = "Get product questions", description = "Retrieve paginated Q&A for a product")
    @GetMapping("/products/{productId}/questions")
    public ResponseEntity<Page<ProductQuestionResponseDto>> getQuestions(
            @PathVariable Long productId, Pageable pageable) {
        Page<ProductQuestionResponseDto> questions = productQAService.getQuestionsByProductId(productId, pageable);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Ask question", description = "Submit a new question about a product")
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

    @Operation(summary = "Answer question", description = "Submit an answer to a product question")
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

    @Operation(summary = "Mark answer helpful", description = "Vote an answer as helpful")
    @PostMapping("/answers/{answerId}/helpful")
    public ResponseEntity<Void> markAnswerHelpful(@PathVariable Long answerId) {
        productQAService.markAnswerHelpful(answerId);
        return ResponseEntity.ok().build();
    }
}
