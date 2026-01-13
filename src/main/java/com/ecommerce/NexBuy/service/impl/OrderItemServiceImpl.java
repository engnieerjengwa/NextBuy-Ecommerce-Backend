package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.OrderItemRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderItemResponseDto;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.OrderItemRepository;
import com.ecommerce.NexBuy.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponseDto> findAll() {
        return orderItemRepository.findAll().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));
        return modelMapper.map(orderItem, OrderItemResponseDto.class);
    }

    @Override
    @Transactional
    public OrderItemResponseDto save(OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = modelMapper.map(orderItemRequestDto, OrderItem.class);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return modelMapper.map(savedOrderItem, OrderItemResponseDto.class);
    }

    @Override
    @Transactional
    public OrderItemResponseDto update(Long id, OrderItemRequestDto orderItemRequestDto) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + id));
        
        modelMapper.map(orderItemRequestDto, existingOrderItem);
        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);
        return modelMapper.map(updatedOrderItem, OrderItemResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }
}