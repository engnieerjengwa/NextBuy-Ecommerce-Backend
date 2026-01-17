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
        try {
            // Validate input
            if (purchaseRequestDto == null) {
                throw new IllegalArgumentException("Purchase request cannot be null");
            }

            // retrieve the order info from dto
            Order order = purchaseRequestDto.getOrder();
            if (order == null) {
                throw new IllegalArgumentException("Order cannot be null");
            }

            // generate tracking number
            String orderTrackingNumber = generateOrderTrackingNumber();
            order.setOrderTrackingNumber(orderTrackingNumber);

            // populate order with order items
            Set<OrderItem> orderItems = purchaseRequestDto.getOrderItems();
            if (orderItems == null || orderItems.isEmpty()) {
                throw new IllegalArgumentException("Order items cannot be null or empty");
            }
            orderItems.forEach(item -> order.addOrderItem(item));

            // populate order with shipping address and billing address
            Address shippingAddress = purchaseRequestDto.getShippingAddress();
            Address billingAddress = purchaseRequestDto.getBillingAddress();

            if (shippingAddress == null) {
                throw new IllegalArgumentException("Shipping address cannot be null");
            }
            if (billingAddress == null) {
                throw new IllegalArgumentException("Billing address cannot be null");
            }

            order.setShippingAddress(shippingAddress);
            order.setBillingAddress(billingAddress);

            // populate customer with order
            Customer customer = purchaseRequestDto.getCustomer();
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null");
            }

            // check if customer already exists
            String email = customer.getEmail();
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer email cannot be null or empty");
            }

            Customer existingCustomer = customerRepository.findByEmail(email);

            if (existingCustomer != null) {
                // use existing customer
                existingCustomer.setFirstName(customer.getFirstName());
                existingCustomer.setLastName(customer.getLastName());
                existingCustomer.addOrder(order);
                customer = existingCustomer;
            } else {
                // this is a new customer
                customer.addOrder(order);
            }

            // save to the database
            customerRepository.save(customer);

            // return the response
            PurchaseResponseDto responseDto = new PurchaseResponseDto();
            responseDto.setOrderTrackingNumber(orderTrackingNumber);
            return responseDto;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error processing order: " + e.getMessage());
            e.printStackTrace();

            // Rethrow as a more specific exception if needed
            throw new RuntimeException("Error processing order: " + e.getMessage(), e);
        }
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number (UUID version-4)
        return randomUUID().toString();
    }
}
