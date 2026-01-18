package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.PaymentInfoRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequestDto paymentInfoRequestDto) throws StripeException {
        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfoRequestDto);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }
}