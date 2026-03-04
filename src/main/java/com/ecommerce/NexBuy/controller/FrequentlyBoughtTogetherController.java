package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.FrequentlyBoughtTogetherResponseDto;
import com.ecommerce.NexBuy.service.FrequentlyBoughtTogetherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class FrequentlyBoughtTogetherController {

    private final FrequentlyBoughtTogetherService frequentlyBoughtTogetherService;

    public FrequentlyBoughtTogetherController(FrequentlyBoughtTogetherService frequentlyBoughtTogetherService) {
        this.frequentlyBoughtTogetherService = frequentlyBoughtTogetherService;
    }

    @GetMapping("/{productId}/frequently-bought-together")
    public ResponseEntity<List<FrequentlyBoughtTogetherResponseDto>> getFrequentlyBoughtTogether(
            @PathVariable Long productId) {
        List<FrequentlyBoughtTogetherResponseDto> recommendations = 
                frequentlyBoughtTogetherService.getFrequentlyBoughtTogether(productId);
        return ResponseEntity.ok(recommendations);
    }
}
