package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.GuestCheckoutRequestDto;
import com.ecommerce.NexBuy.dto.request.PaymentInfoRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Checkout", description = "Order placement, payment processing, and guest checkout")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final ProductRepository productRepository;

    public CheckoutController(CheckoutService checkoutService, ProductRepository productRepository) {
        this.checkoutService = checkoutService;
        this.productRepository = productRepository;
    }

    @Operation(summary = "Place order", description = "Submit an authenticated order with payment")
    @PostMapping("/purchase")
    public PurchaseResponseDto placeOrder(@Valid @RequestBody PurchaseRequestDto purchaseRequestDto) {
        return checkoutService.placeOrder(purchaseRequestDto);
    }

    @Operation(summary = "Create payment intent", description = "Create a Stripe payment intent for checkout")
    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequestDto paymentInfoRequestDto) throws StripeException {
        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfoRequestDto);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }

    @Operation(summary = "Validate stock", description = "Pre-checkout stock validation for all cart items")
    @PostMapping("/validate-stock")
    public ResponseEntity<?> validateStock(@RequestBody List<StockValidationRequest> items) {
        List<StockValidationError> errors = new ArrayList<>();

        for (StockValidationRequest item : items) {
            Optional<Product> productOpt = productRepository.findById(item.productId());
            if (productOpt.isEmpty()) {
                errors.add(new StockValidationError(item.productId(), "unknown",
                        "Product not found", 0, item.requestedQuantity()));
            } else {
                Product product = productOpt.get();
                if (!product.isActive()) {
                    errors.add(new StockValidationError(item.productId(), product.getName(),
                            "Product is no longer available", 0, item.requestedQuantity()));
                } else if (product.getUnitsInStock() < item.requestedQuantity()) {
                    errors.add(new StockValidationError(item.productId(), product.getName(),
                            "Insufficient stock", product.getUnitsInStock(), item.requestedQuantity()));
                }
            }
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("valid", false, "errors", errors));
        }

        return ResponseEntity.ok(Map.of("valid", true));
    }

    @Operation(summary = "Guest checkout", description = "Place an order without authentication")
    @PostMapping("/guest")
    public ResponseEntity<PurchaseResponseDto> placeGuestOrder(
            @Valid @RequestBody GuestCheckoutRequestDto guestCheckoutRequestDto) {
        PurchaseResponseDto responseDto = checkoutService.placeGuestOrder(guestCheckoutRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    record StockValidationRequest(Long productId, int requestedQuantity) {}
    record StockValidationError(Long productId, String productName, String message,
                                 int availableStock, int requestedQuantity) {}
}