package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrderWithNewCustomer() {
        // Arrange
        String email = "test@example.com";
        
        // Create customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail(email);
        
        // Create order
        Order order = new Order();
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setTotalQuantity(2);
        
        // Create order items
        Set<OrderItem> orderItems = new HashSet<>();
        OrderItem item1 = new OrderItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        item1.setUnitPrice(new BigDecimal("50.00"));
        orderItems.add(item1);
        
        OrderItem item2 = new OrderItem();
        item2.setProductId(2L);
        item2.setQuantity(1);
        item2.setUnitPrice(new BigDecimal("50.00"));
        orderItems.add(item2);
        
        // Create addresses
        Address shippingAddress = new Address();
        shippingAddress.setStreet("123 Main St");
        shippingAddress.setCity("Anytown");
        shippingAddress.setState("CA");
        shippingAddress.setCountry("USA");
        shippingAddress.setZipCode("12345");
        
        Address billingAddress = new Address();
        billingAddress.setStreet("123 Main St");
        billingAddress.setCity("Anytown");
        billingAddress.setState("CA");
        billingAddress.setCountry("USA");
        billingAddress.setZipCode("12345");
        
        // Create purchase request
        PurchaseRequestDto purchaseRequest = new PurchaseRequestDto();
        purchaseRequest.setCustomer(customer);
        purchaseRequest.setOrder(order);
        purchaseRequest.setOrderItems(orderItems);
        purchaseRequest.setShippingAddress(shippingAddress);
        purchaseRequest.setBillingAddress(billingAddress);
        
        // Mock repository behavior - no existing customer
        when(customerRepository.findByEmail(email)).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        // Act
        PurchaseResponseDto response = checkoutService.placeOrder(purchaseRequest);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        
        // Verify repository calls
        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    void testPlaceOrderWithExistingCustomer() {
        // Arrange
        String email = "test@example.com";
        
        // Create existing customer
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setFirstName("John");
        existingCustomer.setLastName("Doe");
        existingCustomer.setEmail(email);
        existingCustomer.setOrders(new HashSet<>());
        
        // Create new customer info (same email, different name)
        Customer newCustomerInfo = new Customer();
        newCustomerInfo.setFirstName("Jane"); // Different first name
        newCustomerInfo.setLastName("Smith"); // Different last name
        newCustomerInfo.setEmail(email); // Same email
        
        // Create order
        Order order = new Order();
        order.setTotalPrice(new BigDecimal("200.00"));
        order.setTotalQuantity(1);
        
        // Create order items
        Set<OrderItem> orderItems = new HashSet<>();
        OrderItem item = new OrderItem();
        item.setProductId(3L);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("200.00"));
        orderItems.add(item);
        
        // Create addresses
        Address shippingAddress = new Address();
        shippingAddress.setStreet("456 Oak Ave");
        shippingAddress.setCity("Othertown");
        shippingAddress.setState("NY");
        shippingAddress.setCountry("USA");
        shippingAddress.setZipCode("67890");
        
        Address billingAddress = new Address();
        billingAddress.setStreet("456 Oak Ave");
        billingAddress.setCity("Othertown");
        billingAddress.setState("NY");
        billingAddress.setCountry("USA");
        billingAddress.setZipCode("67890");
        
        // Create purchase request
        PurchaseRequestDto purchaseRequest = new PurchaseRequestDto();
        purchaseRequest.setCustomer(newCustomerInfo);
        purchaseRequest.setOrder(order);
        purchaseRequest.setOrderItems(orderItems);
        purchaseRequest.setShippingAddress(shippingAddress);
        purchaseRequest.setBillingAddress(billingAddress);
        
        // Mock repository behavior - existing customer found
        when(customerRepository.findByEmail(email)).thenReturn(existingCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            // Verify that the existing customer was updated with new info
            assertEquals("Jane", savedCustomer.getFirstName());
            assertEquals("Smith", savedCustomer.getLastName());
            assertEquals(1L, savedCustomer.getId()); // ID should be preserved
            assertEquals(1, savedCustomer.getOrders().size()); // Order should be added
            return savedCustomer;
        });
        
        // Act
        PurchaseResponseDto response = checkoutService.placeOrder(purchaseRequest);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        
        // Verify repository calls
        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}