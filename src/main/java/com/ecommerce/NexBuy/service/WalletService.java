package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.WalletResponseDto;

public interface WalletService {

    WalletResponseDto getWallet(String customerEmail);

    WalletResponseDto getOrCreateWallet(String customerEmail);
}
