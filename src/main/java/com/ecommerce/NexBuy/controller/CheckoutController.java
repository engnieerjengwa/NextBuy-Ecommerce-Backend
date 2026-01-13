package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponseDto placeOrder(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        return checkoutService.placeOrder(purchaseRequestDto);
    }
}