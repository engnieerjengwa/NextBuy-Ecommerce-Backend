package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.PaymentInfoRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.service.CheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.UUID.randomUUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
    private static final List<String> PAYMENT_METHODS = Collections.singletonList("card");

    private final CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String stripeSecretKey) {
        this.customerRepository = customerRepository;
        Stripe.apiKey = stripeSecretKey;
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
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while processing order: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error processing order: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing order: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfoRequestDto paymentInfoRequestDto) throws StripeException {
        if (paymentInfoRequestDto == null) {
            logger.error("Payment info request is null");
            throw new IllegalArgumentException("Payment info request cannot be null");
        }

        // Create payment intent parameters
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfoRequestDto.getAmount());
        params.put("currency", paymentInfoRequestDto.getCurrency());
        params.put("payment_method_types", PAYMENT_METHODS);
        params.put("receipt_email", paymentInfoRequestDto.getReceiptEmail());

        logger.debug("Creating payment intent for amount: {}, currency: {}", 
                    paymentInfoRequestDto.getAmount(), paymentInfoRequestDto.getCurrency());
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        return randomUUID().toString();
    }
}
