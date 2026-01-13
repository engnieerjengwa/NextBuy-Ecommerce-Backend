package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.CustomerRequestDto;
import com.ecommerce.NexBuy.dto.response.CustomerResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        return customerRepository.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    @Override
    @Transactional
    public CustomerResponseDto save(CustomerRequestDto customerRequestDto) {
        Customer customer = modelMapper.map(customerRequestDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    @Override
    @Transactional
    public CustomerResponseDto update(Long id, CustomerRequestDto customerRequestDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        
        modelMapper.map(customerRequestDto, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return modelMapper.map(updatedCustomer, CustomerResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}