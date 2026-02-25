package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.AddressRequestDto;
import com.ecommerce.NexBuy.dto.request.OrderItemRequestDto;
import com.ecommerce.NexBuy.dto.request.PaymentInfoRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.CheckoutService;
import com.ecommerce.NexBuy.service.EmailService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.UUID.randomUUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
    private static final List<String> PAYMENT_METHODS = Collections.singletonList("card");

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               EmailService emailService,
                               @Value("${stripe.key.secret}") String stripeSecretKey) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
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

            // Map DTO to Order entity
            PurchaseRequestDto.OrderSummaryDto orderSummary = purchaseRequestDto.getOrder();
            if (orderSummary == null) {
                throw new IllegalArgumentException("Order cannot be null");
            }

            Order order = new Order();
            order.setTotalPrice(orderSummary.getTotalPrice());
            order.setTotalQuantity(orderSummary.getTotalQuantity());
            order.setStatus("PROCESSING");

            // Generate tracking number
            String orderTrackingNumber = generateOrderTrackingNumber();
            order.setOrderTrackingNumber(orderTrackingNumber);

            // Map and add order items + decrement stock
            Set<OrderItemRequestDto> orderItemDtos = purchaseRequestDto.getOrderItems();
            if (orderItemDtos == null || orderItemDtos.isEmpty()) {
                throw new IllegalArgumentException("Order items cannot be null or empty");
            }

            for (OrderItemRequestDto itemDto : orderItemDtos) {
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setUnitPrice(itemDto.getUnitPrice());
                orderItem.setQuantity(itemDto.getQuantity());
                orderItem.setProductId(itemDto.getProductId());
                order.addOrderItem(orderItem);

                // Decrement stock (TD-13)
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Product not found with ID: " + itemDto.getProductId()));

                if (product.getUnitsInStock() < itemDto.getQuantity()) {
                    throw new IllegalArgumentException(
                            "Insufficient stock for product: " + product.getName()
                                    + ". Available: " + product.getUnitsInStock()
                                    + ", Requested: " + itemDto.getQuantity());
                }

                product.setUnitsInStock(product.getUnitsInStock() - itemDto.getQuantity());
                productRepository.save(product);
            }

            // Map addresses
            Address shippingAddress = mapAddress(purchaseRequestDto.getShippingAddress());
            Address billingAddress = mapAddress(purchaseRequestDto.getBillingAddress());

            if (shippingAddress == null) {
                throw new IllegalArgumentException("Shipping address cannot be null");
            }
            if (billingAddress == null) {
                throw new IllegalArgumentException("Billing address cannot be null");
            }

            order.setShippingAddress(shippingAddress);
            order.setBillingAddress(billingAddress);

            // Map customer
            var customerDto = purchaseRequestDto.getCustomer();
            if (customerDto == null) {
                throw new IllegalArgumentException("Customer cannot be null");
            }

            String email = customerDto.getEmail();
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer email cannot be null or empty");
            }

            Customer customer = customerRepository.findByEmail(email);

            if (customer != null) {
                // Use existing customer — update name
                customer.setFirstName(customerDto.getFirstName());
                customer.setLastName(customerDto.getLastName());
                customer.addOrder(order);
            } else {
                // New customer
                customer = new Customer();
                customer.setFirstName(customerDto.getFirstName());
                customer.setLastName(customerDto.getLastName());
                customer.setEmail(customerDto.getEmail());
                customer.setMobileNumber(customerDto.getMobileNumber());
                customer.addOrder(order);
            }

            // Save to the database
            customerRepository.save(customer);

            // Send order confirmation email (TD-2)
            try {
                String customerName = customer.getFirstName() + " " + customer.getLastName();
                double totalPrice = orderSummary.getTotalPrice() != null
                        ? orderSummary.getTotalPrice().doubleValue() : 0.0;
                int totalQuantity = orderSummary.getTotalQuantity();

                emailService.sendReceiptEmail(
                        email,
                        "NexBuy - Order Confirmation #" + orderTrackingNumber,
                        customerName,
                        orderTrackingNumber,
                        totalPrice,
                        totalQuantity
                );
                logger.info("Order confirmation email sent to: {}", email);
            } catch (Exception e) {
                // Log but don't fail the order if email fails
                logger.error("Failed to send order confirmation email to {}: {}", email, e.getMessage());
            }

            // Return the response
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

    private Address mapAddress(AddressRequestDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());
        return address;
    }

    private String generateOrderTrackingNumber() {
        return randomUUID().toString();
    }
}
