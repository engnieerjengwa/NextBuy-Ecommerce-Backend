package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

@Data
public class CustomerRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
}