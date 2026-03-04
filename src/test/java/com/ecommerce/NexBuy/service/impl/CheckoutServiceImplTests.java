package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.AddressRequestDto;
import com.ecommerce.NexBuy.dto.request.CustomerRequestDto;
import com.ecommerce.NexBuy.dto.request.OrderItemRequestDto;
import com.ecommerce.NexBuy.dto.request.PurchaseRequestDto;
import com.ecommerce.NexBuy.dto.response.PurchaseResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Product;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.ProductRepository;
import com.ecommerce.NexBuy.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CheckoutServiceImplTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EmailService emailService;

    private CheckoutServiceImpl checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkoutService = new CheckoutServiceImpl(customerRepository, productRepository, emailService, "sk_test_mockKey");
    }

    @Test
    void testPlaceOrderWithNewCustomer() {
        // Arrange
        String email = "test@example.com";
        
        // Create customer DTO
        CustomerRequestDto customerDto = new CustomerRequestDto();
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setEmail(email);
        
        // Create order summary DTO
        PurchaseRequestDto.OrderSummaryDto orderSummary = new PurchaseRequestDto.OrderSummaryDto();
        orderSummary.setTotalPrice(new BigDecimal("100.00"));
        orderSummary.setTotalQuantity(2);
        
        // Create order item DTOs
        Set<OrderItemRequestDto> orderItems = new HashSet<>();
        OrderItemRequestDto item1 = new OrderItemRequestDto();
        item1.setProductId(1L);
        item1.setQuantity(1);
        item1.setUnitPrice(new BigDecimal("50.00"));
        orderItems.add(item1);
        
        OrderItemRequestDto item2 = new OrderItemRequestDto();
        item2.setProductId(2L);
        item2.setQuantity(1);
        item2.setUnitPrice(new BigDecimal("50.00"));
        orderItems.add(item2);
        
        // Create address DTOs
        AddressRequestDto shippingAddress = new AddressRequestDto();
        shippingAddress.setStreet("123 Main St");
        shippingAddress.setCity("Anytown");
        shippingAddress.setState("CA");
        shippingAddress.setCountry("USA");
        shippingAddress.setZipCode("12345");
        
        AddressRequestDto billingAddress = new AddressRequestDto();
        billingAddress.setStreet("123 Main St");
        billingAddress.setCity("Anytown");
        billingAddress.setState("CA");
        billingAddress.setCountry("USA");
        billingAddress.setZipCode("12345");
        
        // Create purchase request
        PurchaseRequestDto purchaseRequest = new PurchaseRequestDto();
        purchaseRequest.setCustomer(customerDto);
        purchaseRequest.setOrder(orderSummary);
        purchaseRequest.setOrderItems(orderItems);
        purchaseRequest.setShippingAddress(shippingAddress);
        purchaseRequest.setBillingAddress(billingAddress);
        
        // Mock product repository for stock decrement
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setUnitsInStock(10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setUnitsInStock(10);
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Mock customer repository - no existing customer
        when(customerRepository.findByEmail(email)).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Mock email service (void method - no-op by default)
        doNothing().when(emailService).sendReceiptEmail(anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt());
        
        // Act
        PurchaseResponseDto response = checkoutService.placeOrder(purchaseRequest);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        
        // Verify repository calls
        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(productRepository, times(2)).findById(anyLong());
        verify(productRepository, times(2)).save(any(Product.class));
    }
    
    @Test
    void testPlaceOrderWithExistingCustomer() {
        // Arrange
        String email = "test@example.com";
        
        // Create existing customer entity (returned by mock repo)
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setFirstName("John");
        existingCustomer.setLastName("Doe");
        existingCustomer.setEmail(email);
        existingCustomer.setOrders(new HashSet<>());
        
        // Create customer DTO with new info (same email, different name)
        CustomerRequestDto customerDto = new CustomerRequestDto();
        customerDto.setFirstName("Jane");
        customerDto.setLastName("Smith");
        customerDto.setEmail(email);
        
        // Create order summary DTO
        PurchaseRequestDto.OrderSummaryDto orderSummary = new PurchaseRequestDto.OrderSummaryDto();
        orderSummary.setTotalPrice(new BigDecimal("200.00"));
        orderSummary.setTotalQuantity(1);
        
        // Create order item DTOs
        Set<OrderItemRequestDto> orderItems = new HashSet<>();
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setProductId(3L);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("200.00"));
        orderItems.add(item);
        
        // Create address DTOs
        AddressRequestDto shippingAddress = new AddressRequestDto();
        shippingAddress.setStreet("456 Oak Ave");
        shippingAddress.setCity("Othertown");
        shippingAddress.setState("NY");
        shippingAddress.setCountry("USA");
        shippingAddress.setZipCode("67890");
        
        AddressRequestDto billingAddress = new AddressRequestDto();
        billingAddress.setStreet("456 Oak Ave");
        billingAddress.setCity("Othertown");
        billingAddress.setState("NY");
        billingAddress.setCountry("USA");
        billingAddress.setZipCode("67890");
        
        // Create purchase request
        PurchaseRequestDto purchaseRequest = new PurchaseRequestDto();
        purchaseRequest.setCustomer(customerDto);
        purchaseRequest.setOrder(orderSummary);
        purchaseRequest.setOrderItems(orderItems);
        purchaseRequest.setShippingAddress(shippingAddress);
        purchaseRequest.setBillingAddress(billingAddress);
        
        // Mock product repository for stock decrement
        Product product3 = new Product();
        product3.setId(3L);
        product3.setName("Product 3");
        product3.setUnitsInStock(5);
        when(productRepository.findById(3L)).thenReturn(Optional.of(product3));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Mock customer repository - existing customer found
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
        
        // Mock email service
        doNothing().when(emailService).sendReceiptEmail(anyString(), anyString(), anyString(), anyString(), anyDouble(), anyInt());
        
        // Act
        PurchaseResponseDto response = checkoutService.placeOrder(purchaseRequest);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getOrderTrackingNumber());
        
        // Verify repository calls
        verify(customerRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}