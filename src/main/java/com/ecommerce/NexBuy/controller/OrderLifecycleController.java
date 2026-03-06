package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.OrderStatusUpdateRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderResponseDto;
import com.ecommerce.NexBuy.dto.response.OrderStatusHistoryResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.OrderLifecycleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Orders", description = "Order tracking, cancellation, reorder, and status management")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/orders")
public class OrderLifecycleController {

    private final OrderLifecycleService orderLifecycleService;

    public OrderLifecycleController(OrderLifecycleService orderLifecycleService) {
        this.orderLifecycleService = orderLifecycleService;
    }

    @Operation(summary = "Get order tracking", description = "Retrieve the full status timeline for an order")
    @GetMapping("/{orderId}/tracking")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderStatusHistoryResponseDto>> getOrderTracking(
            @PathVariable Long orderId) {
        List<OrderStatusHistoryResponseDto> history = orderLifecycleService.getOrderStatusHistory(orderId);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Cancel order", description = "Cancel an order before it has been shipped")
    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        OrderResponseDto order = orderLifecycleService.cancelOrder(userDetails.getEmail(), orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Re-order", description = "Create a new order from a previous order's items")
    @PostMapping("/{orderId}/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> reorder(
            @PathVariable Long orderId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        OrderResponseDto order = orderLifecycleService.reorderFromPrevious(userDetails.getEmail(), orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Update order status", description = "Update the status of an order (admin only)")
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto requestDto) {
        requestDto.setOrderId(orderId);
        OrderResponseDto order = orderLifecycleService.updateOrderStatus(requestDto);
        return ResponseEntity.ok(order);
    }
}
