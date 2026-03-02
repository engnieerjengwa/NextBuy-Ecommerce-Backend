package com.ecommerce.NexBuy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavedAddressRequestDto {

    @Size(max = 50, message = "Label must be at most 50 characters")
    private String label;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street must be at most 255 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @Size(max = 100, message = "Province must be at most 100 characters")
    private String province;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    @Size(max = 20, message = "Zip code must be at most 20 characters")
    private String zipCode;

    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phoneNumber;

    private Boolean isDefault = false;
}
