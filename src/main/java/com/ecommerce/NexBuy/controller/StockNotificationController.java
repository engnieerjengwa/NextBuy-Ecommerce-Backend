package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.StockNotificationRequestDto;
import com.ecommerce.NexBuy.service.StockNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Stock Notifications", description = "Out-of-stock restock notification subscriptions")
@RestController
@RequestMapping("/api/products")
public class StockNotificationController {

    private final StockNotificationService stockNotificationService;

    public StockNotificationController(StockNotificationService stockNotificationService) {
        this.stockNotificationService = stockNotificationService;
    }

    @Operation(summary = "Subscribe to restock", description = "Subscribe to back-in-stock notification for a product")
    @PostMapping("/{productId}/notify-restock")
    public ResponseEntity<Map<String, String>> subscribe(
            @PathVariable Long productId,
            @Valid @RequestBody StockNotificationRequestDto requestDto) {
        requestDto.setProductId(productId);
        stockNotificationService.subscribe(requestDto);
        return new ResponseEntity<>(
                Map.of("message", "You will be notified when this product is back in stock."),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Check subscription", description = "Check if an email is subscribed for restock notifications")
    @GetMapping("/{productId}/notify-restock")
    public ResponseEntity<Map<String, Boolean>> isSubscribed(
            @PathVariable Long productId,
            @RequestParam String email) {
        boolean subscribed = stockNotificationService.isSubscribed(productId, email);
        return ResponseEntity.ok(Map.of("subscribed", subscribed));
    }

    @Operation(summary = "Unsubscribe from restock", description = "Unsubscribe from back-in-stock notifications")
    @DeleteMapping("/{productId}/notify-restock")
    public ResponseEntity<Map<String, String>> unsubscribe(
            @PathVariable Long productId,
            @RequestParam String email) {
        stockNotificationService.unsubscribe(productId, email);
        return ResponseEntity.ok(
                Map.of("message", "You have been unsubscribed from restock notifications for this product."));
    }
}
