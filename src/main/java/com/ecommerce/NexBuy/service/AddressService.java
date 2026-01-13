package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.AddressRequestDto;
import com.ecommerce.NexBuy.dto.response.AddressResponseDto;

import java.util.List;

public interface AddressService {
    List<AddressResponseDto> findAll();
    AddressResponseDto findById(Long id);
    AddressResponseDto save(AddressRequestDto addressRequestDto);
    AddressResponseDto update(Long id, AddressRequestDto addressRequestDto);
    void deleteById(Long id);
}