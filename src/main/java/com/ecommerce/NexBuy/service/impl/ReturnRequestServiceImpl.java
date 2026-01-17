package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.ReturnItemRequestDto;
import com.ecommerce.NexBuy.dto.request.ReturnRequestRequestDto;
import com.ecommerce.NexBuy.dto.response.ReturnRequestResponseDto;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.entity.ReturnItem;
import com.ecommerce.NexBuy.entity.ReturnRequest;
import com.ecommerce.NexBuy.repo.OrderItemRepository;
import com.ecommerce.NexBuy.repo.OrderRepository;
import com.ecommerce.NexBuy.repo.ReturnRequestRepository;
import com.ecommerce.NexBuy.service.ReturnRequestService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReturnRequestServiceImpl implements ReturnRequestService {

    private final ReturnRequestRepository returnRequestRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReturnRequestServiceImpl(ReturnRequestRepository returnRequestRepository,
                                   OrderRepository orderRepository,
                                   OrderItemRepository orderItemRepository,
                                   ModelMapper modelMapper) {
        this.returnRequestRepository = returnRequestRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ReturnRequestResponseDto createReturnRequest(ReturnRequestRequestDto returnRequestRequestDto) {
        // Find the order
        Order order = orderRepository.findById(returnRequestRequestDto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + returnRequestRequestDto.getOrderId()));

        // Check if order is eligible for return
        if (!"DELIVERED".equalsIgnoreCase(order.getDeliveryStatus())) {
            throw new IllegalStateException("Order must be delivered to be eligible for return");
        }

        // Check if order is already returned
        if (Boolean.TRUE.equals(order.getIsReturned())) {
            throw new IllegalStateException("Order is already returned");
        }

        // Check if order is within return window (e.g., 30 days)
        LocalDateTime deliveryDate = order.getDeliveryDate();
        if (deliveryDate != null) {
            LocalDateTime returnDeadline = deliveryDate.plusDays(30); // Configurable return window
            if (LocalDateTime.now().isAfter(returnDeadline)) {
                throw new IllegalStateException("Return window has expired");
            }
        }

        // Create return request
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setOrder(order);
        returnRequest.setReturnReason(returnRequestRequestDto.getReturnReason());
        returnRequest.setComments(returnRequestRequestDto.getComments());
        returnRequest.setStatus("PENDING");

        // Process return items
        for (ReturnItemRequestDto itemDto : returnRequestRequestDto.getReturnItems()) {
            OrderItem orderItem = orderItemRepository.findById(itemDto.getOrderItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: " + itemDto.getOrderItemId()));

            // Validate that the order item belongs to the order
            if (!orderItem.getOrder().getId().equals(order.getId())) {
                throw new IllegalArgumentException("Order item does not belong to the specified order");
            }

            // Validate return quantity
            if (itemDto.getQuantity() <= 0 || itemDto.getQuantity() > orderItem.getQuantity()) {
                throw new IllegalArgumentException("Invalid return quantity for item: " + orderItem.getProductId());
            }

            ReturnItem returnItem = new ReturnItem();
            returnItem.setOrderItem(orderItem);
            returnItem.setQuantity(itemDto.getQuantity());
            returnItem.setReason(itemDto.getReason());
            returnRequest.addReturnItem(returnItem);
        }

        // Mark order as returned
        order.setIsReturned(true);

        // Save return request
        ReturnRequest savedReturnRequest = returnRequestRepository.save(returnRequest);

        // Map to response DTO
        ReturnRequestResponseDto responseDto = modelMapper.map(savedReturnRequest, ReturnRequestResponseDto.class);
        responseDto.setOrderId(order.getId());
        responseDto.setOrderTrackingNumber(order.getOrderTrackingNumber());

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnRequestResponseDto getReturnRequestById(Long id) {
        ReturnRequest returnRequest = returnRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return request not found with id: " + id));

        ReturnRequestResponseDto responseDto = modelMapper.map(returnRequest, ReturnRequestResponseDto.class);
        responseDto.setOrderId(returnRequest.getOrder().getId());
        responseDto.setOrderTrackingNumber(returnRequest.getOrder().getOrderTrackingNumber());

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReturnRequestResponseDto> getReturnRequestsByOrderId(Long orderId, Pageable pageable) {
        Page<ReturnRequest> returnRequests = returnRequestRepository.findByOrderId(orderId, pageable);

        return returnRequests.map(returnRequest -> {
            ReturnRequestResponseDto responseDto = modelMapper.map(returnRequest, ReturnRequestResponseDto.class);
            responseDto.setOrderId(returnRequest.getOrder().getId());
            responseDto.setOrderTrackingNumber(returnRequest.getOrder().getOrderTrackingNumber());
            return responseDto;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReturnRequestResponseDto> getReturnRequestsByCustomerEmail(String email, Pageable pageable) {
        Page<ReturnRequest> returnRequests = returnRequestRepository.findByOrderCustomerEmail(email, pageable);

        return returnRequests.map(returnRequest -> {
            ReturnRequestResponseDto responseDto = modelMapper.map(returnRequest, ReturnRequestResponseDto.class);
            responseDto.setOrderId(returnRequest.getOrder().getId());
            responseDto.setOrderTrackingNumber(returnRequest.getOrder().getOrderTrackingNumber());
            return responseDto;
        });
    }

    @Override
    @Transactional
    public ReturnRequestResponseDto updateReturnRequestStatus(Long id, String status) {
        ReturnRequest returnRequest = returnRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return request not found with id: " + id));

        // Validate status
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        returnRequest.setStatus(status);
        ReturnRequest updatedReturnRequest = returnRequestRepository.save(returnRequest);

        ReturnRequestResponseDto responseDto = modelMapper.map(updatedReturnRequest, ReturnRequestResponseDto.class);
        responseDto.setOrderId(updatedReturnRequest.getOrder().getId());
        responseDto.setOrderTrackingNumber(updatedReturnRequest.getOrder().getOrderTrackingNumber());

        return responseDto;
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) || "APPROVED".equals(status) || 
               "REJECTED".equals(status) || "COMPLETED".equals(status);
    }
}