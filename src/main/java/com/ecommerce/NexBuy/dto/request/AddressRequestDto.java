package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequestDto {
    @NotBlank(message = "Street is required")
    @Size(min = 2, message = "Street must be at least 2 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 2, message = "City must be at least 2 characters")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Zip code is required")
    @Size(min = 2, message = "Zip code must be at least 2 characters")
    private String zipCode;
}