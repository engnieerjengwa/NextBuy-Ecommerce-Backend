package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.OrderStatusUpdateRequestDto;
import com.ecommerce.NexBuy.dto.response.OrderResponseDto;
import com.ecommerce.NexBuy.dto.response.OrderStatusHistoryResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderStatus;
import com.ecommerce.NexBuy.entity.OrderStatusHistory;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.OrderRepository;
import com.ecommerce.NexBuy.repo.OrderStatusHistoryRepository;
import com.ecommerce.NexBuy.service.OrderLifecycleService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderLifecycleServiceImpl implements OrderLifecycleService {

    private static final Logger logger = LoggerFactory.getLogger(OrderLifecycleServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderLifecycleServiceImpl(OrderRepository orderRepository,
                                     OrderStatusHistoryRepository orderStatusHistoryRepository,
                                     CustomerRepository customerRepository,
                                     ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(OrderStatusUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + requestDto.getOrderId()));

        String oldStatusStr = order.getStatus();
        OrderStatus newStatus = OrderStatus.fromString(requestDto.getStatus());

        // Validate status transition
        OrderStatus currentStatus = OrderStatus.fromString(oldStatusStr);
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid status transition: cannot change from " + currentStatus + " to " + newStatus);
        }

        order.setStatus(newStatus.name());
        orderRepository.save(order);

        // Record status history
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(newStatus.name());
        history.setNote(requestDto.getNote() != null ? requestDto.getNote() : "Status changed from " + oldStatusStr + " to " + newStatus.name());
        orderStatusHistoryRepository.save(history);

        logger.info("Order {} status updated from {} to {}", order.getId(), oldStatusStr, newStatus.name());
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public List<OrderStatusHistoryResponseDto> getOrderStatusHistory(Long orderId) {
        List<OrderStatusHistory> historyList = orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtAsc(orderId);
        return historyList.stream()
                .map(h -> {
                    OrderStatusHistoryResponseDto dto = new OrderStatusHistoryResponseDto();
                    dto.setId(h.getId());
                    dto.setStatus(h.getStatus());
                    dto.setNote(h.getNote());
                    dto.setCreatedAt(h.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(String customerEmail, Long orderId) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("You can only cancel your own orders");
        }

        String currentStatus = order.getStatus();
        if ("SHIPPED".equalsIgnoreCase(currentStatus) || "DELIVERED".equalsIgnoreCase(currentStatus)
                || "CANCELLED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalArgumentException("Cannot cancel order with status: " + currentStatus);
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus("CANCELLED");
        history.setNote("Order cancelled by customer");
        orderStatusHistoryRepository.save(history);

        logger.info("Order {} cancelled by customer {}", orderId, customerEmail);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    @Transactional
    public OrderResponseDto reorderFromPrevious(String customerEmail, Long orderId) {
        // This generates a new order with the same items as a previous order
        // The actual cart addition and checkout happens on the frontend
        // This endpoint returns the order details for re-ordering
        Order previousOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        if (!previousOrder.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("You can only reorder your own orders");
        }

        return modelMapper.map(previousOrder, OrderResponseDto.class);
    }
}
