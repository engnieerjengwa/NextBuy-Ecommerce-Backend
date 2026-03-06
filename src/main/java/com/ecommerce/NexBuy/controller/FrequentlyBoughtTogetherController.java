package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.FrequentlyBoughtTogetherResponseDto;
import com.ecommerce.NexBuy.service.FrequentlyBoughtTogetherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Recommendations", description = "Frequently bought together product recommendations")
@RestController
@RequestMapping("/api/products")
public class FrequentlyBoughtTogetherController {

    private final FrequentlyBoughtTogetherService frequentlyBoughtTogetherService;

    public FrequentlyBoughtTogetherController(FrequentlyBoughtTogetherService frequentlyBoughtTogetherService) {
        this.frequentlyBoughtTogetherService = frequentlyBoughtTogetherService;
    }

    @Operation(summary = "Get recommendations", description = "Retrieve products frequently bought together with the given product")
    @GetMapping("/{productId}/frequently-bought-together")
    public ResponseEntity<List<FrequentlyBoughtTogetherResponseDto>> getFrequentlyBoughtTogether(
            @PathVariable Long productId) {
        List<FrequentlyBoughtTogetherResponseDto> recommendations = 
                frequentlyBoughtTogetherService.getFrequentlyBoughtTogether(productId);
        return ResponseEntity.ok(recommendations);
    }
}
