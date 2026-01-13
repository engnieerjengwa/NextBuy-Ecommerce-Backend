package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

@Data
public class AddressResponseDto {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}