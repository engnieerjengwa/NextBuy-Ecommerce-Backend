package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.SavedAddressRequestDto;
import com.ecommerce.NexBuy.dto.response.SavedAddressResponseDto;

import java.util.List;

public interface SavedAddressService {

    List<SavedAddressResponseDto> getAddresses(String customerEmail);

    SavedAddressResponseDto getAddress(String customerEmail, Long addressId);

    SavedAddressResponseDto createAddress(String customerEmail, SavedAddressRequestDto requestDto);

    SavedAddressResponseDto updateAddress(String customerEmail, Long addressId, SavedAddressRequestDto requestDto);

    void deleteAddress(String customerEmail, Long addressId);

    SavedAddressResponseDto setDefault(String customerEmail, Long addressId);
}
