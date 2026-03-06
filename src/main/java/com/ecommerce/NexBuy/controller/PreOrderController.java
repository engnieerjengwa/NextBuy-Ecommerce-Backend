package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.PreOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Preorders", description = "Product preorder and backorder management")
@RestController
@RequestMapping("/api/preorders")
public class PreOrderController {

    private final PreOrderService preOrderService;

    public PreOrderController(PreOrderService preOrderService) {
        this.preOrderService = preOrderService;
    }

    @Operation(summary = "Get preorder products", description = "Retrieve paginated list of products available for preorder")
    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> getPreOrderProducts(Pageable pageable) {
        Page<Map<String, Object>> products = preOrderService.getPreOrderProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get preorder status", description = "Check the preorder availability status of a product")
    @GetMapping("/{productId}/status")
    public ResponseEntity<Map<String, Object>> getPreOrderStatus(@PathVariable Long productId) {
        Map<String, Object> status = preOrderService.getPreOrderStatus(productId);
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Place preorder", description = "Submit a preorder for a product not yet in stock")
    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> placePreOrder(
            Authentication authentication,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        MessageResponseDto result = preOrderService.placePreOrder(userDetails.getEmail(), productId, quantity);
        return ResponseEntity.ok(result);
    }
}
