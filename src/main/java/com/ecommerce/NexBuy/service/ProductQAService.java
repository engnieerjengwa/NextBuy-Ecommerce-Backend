package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.ProductAnswerRequestDto;
import com.ecommerce.NexBuy.dto.request.ProductQuestionRequestDto;
import com.ecommerce.NexBuy.dto.response.ProductAnswerResponseDto;
import com.ecommerce.NexBuy.dto.response.ProductQuestionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQAService {

    Page<ProductQuestionResponseDto> getQuestionsByProductId(Long productId, Pageable pageable);

    ProductQuestionResponseDto askQuestion(String customerEmail, ProductQuestionRequestDto requestDto);

    ProductAnswerResponseDto answerQuestion(String customerEmail, ProductAnswerRequestDto requestDto);

    void markAnswerHelpful(Long answerId);
}
