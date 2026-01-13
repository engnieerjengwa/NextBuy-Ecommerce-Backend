package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.service.CheckoutService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponseDto placeOrder(PurchaseRequestDto purchaseRequestDto) {

        // retrieve the order info from dto
        Order order = purchaseRequestDto.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with order items
        Set<OrderItem> orderItems = purchaseRequestDto.getOrderItems();
        orderItems.forEach(item -> order.addOrderItem(item));

        // populate order with shipping address and billing address
        order.setShippingAddress(purchaseRequestDto.getShippingAddress());
        order.setBillingAddress(purchaseRequestDto.getBillingAddress());

        // populate customer with order
        Customer customer = purchaseRequestDto.getCustomer();
        customer.addOrder(order);

        // save to the database
        customerRepository.save(customer);

        // return the response
        PurchaseResponseDto responseDto = new PurchaseResponseDto();
        responseDto.setOrderTrackingNumber(orderTrackingNumber);
        return responseDto;
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number (UUID version-4)
        return randomUUID().toString();
    }
}
