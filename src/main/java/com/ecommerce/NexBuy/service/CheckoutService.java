package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.PaymentInfoRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponseDto placeOrder(PurchaseRequestDto purchaseRequestDto);
    PaymentIntent createPaymentIntent(PaymentInfoRequestDto paymentInfoRequestDto) throws StripeException;
}
