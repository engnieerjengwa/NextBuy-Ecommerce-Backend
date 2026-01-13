package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.AddressRequestDto;
import com.ecommerce.NexBuy.dto.response.AddressResponseDto;
import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.repo.AddressRepository;
import com.ecommerce.NexBuy.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> findAll() {
        return addressRepository.findAll().stream()
                .map(address -> modelMapper.map(address, AddressResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto findById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressResponseDto.class);
    }

    @Override
    @Transactional
    public AddressResponseDto save(AddressRequestDto addressRequestDto) {
        Address address = modelMapper.map(addressRequestDto, Address.class);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressResponseDto.class);
    }

    @Override
    @Transactional
    public AddressResponseDto update(Long id, AddressRequestDto addressRequestDto) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + id));
        
        modelMapper.map(addressRequestDto, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return modelMapper.map(updatedAddress, AddressResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }
}