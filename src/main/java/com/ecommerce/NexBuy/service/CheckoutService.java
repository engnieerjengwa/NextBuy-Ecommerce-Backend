package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;

public interface CheckoutService {

    PurchaseResponseDto placeOrder(PurchaseRequestDto purchaseRequestDto);
}
