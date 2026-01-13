package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.OrderRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.OrderRepository;
import com.ecommerce.NexBuy.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public OrderResponseDto save(OrderRequestDto orderRequestDto) {
        Order order = modelMapper.map(orderRequestDto, Order.class);

        // Handle customer relationship
        if (orderRequestDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + orderRequestDto.getCustomerId()));
            order.setCustomer(customer);
        }

        // Handle order items relationship
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        } else {
            order.setOrderItems(new HashSet<>());
        }

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public OrderResponseDto update(Long id, OrderRequestDto orderRequestDto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        // Update basic fields
        modelMapper.map(orderRequestDto, existingOrder);

        // Handle customer relationship
        if (orderRequestDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderRequestDto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + orderRequestDto.getCustomerId()));
            existingOrder.setCustomer(customer);
        }

        // Handle order items relationship
        if (orderRequestDto.getOrderItems() != null) {
            // Clear existing items
            existingOrder.getOrderItems().clear();

            // Map and add new items
            Set<OrderItem> orderItems = orderRequestDto.getOrderItems().stream()
                    .map(itemDto -> {
                        OrderItem item = modelMapper.map(itemDto, OrderItem.class);
                        item.setOrder(existingOrder);
                        return item;
                    })
                    .collect(Collectors.toSet());

            existingOrder.setOrderItems(orderItems);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return modelMapper.map(updatedOrder, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
