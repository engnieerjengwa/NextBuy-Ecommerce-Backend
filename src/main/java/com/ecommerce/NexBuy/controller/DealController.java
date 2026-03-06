package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.DealResponseDto;
import com.ecommerce.NexBuy.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Deals", description = "Daily deals, flash sales, and promotions")
@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @Operation(summary = "Get active deals", description = "Retrieve all currently active deals and promotions")
    @GetMapping
    public ResponseEntity<List<DealResponseDto>> getActiveDeals() {
        return ResponseEntity.ok(dealService.getActiveDeals());
    }

    @Operation(summary = "Get daily deals", description = "Retrieve today's daily deal offers")
    @GetMapping("/daily")
    public ResponseEntity<List<DealResponseDto>> getDailyDeals() {
        return ResponseEntity.ok(dealService.getDailyDeals());
    }

    @Operation(summary = "Get flash sales", description = "Retrieve currently running flash sale offers")
    @GetMapping("/flash-sales")
    public ResponseEntity<List<DealResponseDto>> getFlashSales() {
        return ResponseEntity.ok(dealService.getFlashSales());
    }

    @Operation(summary = "Get deal by ID", description = "Retrieve details of a specific deal")
    @GetMapping("/{dealId}")
    public ResponseEntity<DealResponseDto> getDealById(@PathVariable Long dealId) {
        return ResponseEntity.ok(dealService.getDealById(dealId));
    }
}
