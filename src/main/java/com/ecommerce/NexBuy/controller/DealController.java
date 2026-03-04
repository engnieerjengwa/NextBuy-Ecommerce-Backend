package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.DealResponseDto;
import com.ecommerce.NexBuy.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @GetMapping
    public ResponseEntity<List<DealResponseDto>> getActiveDeals() {
        return ResponseEntity.ok(dealService.getActiveDeals());
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DealResponseDto>> getDailyDeals() {
        return ResponseEntity.ok(dealService.getDailyDeals());
    }

    @GetMapping("/flash-sales")
    public ResponseEntity<List<DealResponseDto>> getFlashSales() {
        return ResponseEntity.ok(dealService.getFlashSales());
    }

    @GetMapping("/{dealId}")
    public ResponseEntity<DealResponseDto> getDealById(@PathVariable Long dealId) {
        return ResponseEntity.ok(dealService.getDealById(dealId));
    }
}
