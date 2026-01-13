package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.CustomerRequestDto;
import com.ecommerce.NexBuy.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseDto> findAll();
    CustomerResponseDto findById(Long id);
    CustomerResponseDto save(CustomerRequestDto customerRequestDto);
    CustomerResponseDto update(Long id, CustomerRequestDto customerRequestDto);
    void deleteById(Long id);
}