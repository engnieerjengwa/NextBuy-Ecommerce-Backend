package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class GuestCheckoutRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be at most 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be at most 100 characters")
    private String lastName;

    @Size(max = 20, message = "Mobile number must be at most 20 characters")
    private String mobileNumber;

    @Valid
    @NotNull(message = "Shipping address is required")
    private AddressRequestDto shippingAddress;

    @Valid
    @NotNull(message = "Billing address is required")
    private AddressRequestDto billingAddress;

    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @NotNull(message = "Total quantity is required")
    private Integer totalQuantity;

    @Valid
    @NotNull(message = "Order items are required")
    private Set<OrderItemRequestDto> orderItems;
}
