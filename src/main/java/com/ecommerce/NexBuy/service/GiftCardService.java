package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.GiftCardPurchaseRequestDto;
import com.ecommerce.NexBuy.dto.request.GiftCardRedeemRequestDto;
import com.ecommerce.NexBuy.dto.response.GiftCardResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;

import java.util.List;

public interface GiftCardService {

    GiftCardResponseDto purchaseGiftCard(String purchaserEmail, GiftCardPurchaseRequestDto request);

    MessageResponseDto redeemGiftCard(String customerEmail, GiftCardRedeemRequestDto request);

    GiftCardResponseDto getGiftCardByCode(String code);

    List<GiftCardResponseDto> getMyGiftCards(String email);
}
