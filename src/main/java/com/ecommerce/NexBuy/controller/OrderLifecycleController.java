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

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderLifecycleController {

    private final OrderLifecycleService orderLifecycleService;

    public OrderLifecycleController(OrderLifecycleService orderLifecycleService) {
        this.orderLifecycleService = orderLifecycleService;
    }

    /**
     * Get order status timeline (auth required — customer must own the order)
     */
    @GetMapping("/{orderId}/tracking")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderStatusHistoryResponseDto>> getOrderTracking(
            @PathVariable Long orderId) {
        List<OrderStatusHistoryResponseDto> history = orderLifecycleService.getOrderStatusHistory(orderId);
        return ResponseEntity.ok(history);
    }

    /**
     * Cancel an order (auth required — before SHIPPED status only)
     */
    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        OrderResponseDto order = orderLifecycleService.cancelOrder(userDetails.getEmail(), orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Re-order from a previous order (auth required)
     */
    @PostMapping("/{orderId}/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDto> reorder(
            @PathVariable Long orderId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        OrderResponseDto order = orderLifecycleService.reorderFromPrevious(userDetails.getEmail(), orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Update order status (admin only)
     */
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
