package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.SavedAddressRequestDto;
import com.ecommerce.NexBuy.dto.response.SavedAddressResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.SavedAddress;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.SavedAddressRepository;
import com.ecommerce.NexBuy.service.SavedAddressService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedAddressServiceImpl implements SavedAddressService {

    private static final Logger logger = LoggerFactory.getLogger(SavedAddressServiceImpl.class);

    private final SavedAddressRepository savedAddressRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public SavedAddressServiceImpl(SavedAddressRepository savedAddressRepository,
                                   CustomerRepository customerRepository) {
        this.savedAddressRepository = savedAddressRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<SavedAddressResponseDto> getAddresses(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);
        return savedAddressRepository.findByCustomerIdOrderByIsDefaultDescDateCreatedDesc(customer.getId())
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SavedAddressResponseDto getAddress(String customerEmail, Long addressId) {
        Customer customer = findCustomerByEmail(customerEmail);
        SavedAddress address = savedAddressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
        return mapToResponseDto(address);
    }

    @Override
    @Transactional
    public SavedAddressResponseDto createAddress(String customerEmail, SavedAddressRequestDto requestDto) {
        Customer customer = findCustomerByEmail(customerEmail);

        SavedAddress address = new SavedAddress();
        address.setCustomer(customer);
        mapFromRequestDto(requestDto, address);

        if (Boolean.TRUE.equals(requestDto.getIsDefault())) {
            savedAddressRepository.clearDefaultForCustomer(customer.getId(), 0L);
        }

        SavedAddress saved = savedAddressRepository.save(address);
        logger.info("Address created for customer {}", customerEmail);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional
    public SavedAddressResponseDto updateAddress(String customerEmail, Long addressId, SavedAddressRequestDto requestDto) {
        Customer customer = findCustomerByEmail(customerEmail);
        SavedAddress address = savedAddressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));

        mapFromRequestDto(requestDto, address);

        if (Boolean.TRUE.equals(requestDto.getIsDefault())) {
            savedAddressRepository.clearDefaultForCustomer(customer.getId(), addressId);
        }

        SavedAddress saved = savedAddressRepository.save(address);
        return mapToResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteAddress(String customerEmail, Long addressId) {
        Customer customer = findCustomerByEmail(customerEmail);
        SavedAddress address = savedAddressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
        savedAddressRepository.delete(address);
        logger.info("Address {} deleted for customer {}", addressId, customerEmail);
    }

    @Override
    @Transactional
    public SavedAddressResponseDto setDefault(String customerEmail, Long addressId) {
        Customer customer = findCustomerByEmail(customerEmail);
        SavedAddress address = savedAddressRepository.findByIdAndCustomerId(addressId, customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));

        savedAddressRepository.clearDefaultForCustomer(customer.getId(), addressId);
        address.setIsDefault(true);
        SavedAddress saved = savedAddressRepository.save(address);
        return mapToResponseDto(saved);
    }

    private Customer findCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + email);
        }
        return customer;
    }

    private void mapFromRequestDto(SavedAddressRequestDto dto, SavedAddress address) {
        address.setLabel(dto.getLabel());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setProvince(dto.getProvince());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setIsDefault(dto.getIsDefault());
    }

    private SavedAddressResponseDto mapToResponseDto(SavedAddress address) {
        SavedAddressResponseDto dto = new SavedAddressResponseDto();
        dto.setId(address.getId());
        dto.setLabel(address.getLabel());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setProvince(address.getProvince());
        dto.setCountry(address.getCountry());
        dto.setZipCode(address.getZipCode());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setIsDefault(address.getIsDefault());
        dto.setDateCreated(address.getDateCreated());
        return dto;
    }
}
