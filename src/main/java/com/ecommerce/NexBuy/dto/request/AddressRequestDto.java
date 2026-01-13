package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

@Data
public class AddressRequestDto {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}