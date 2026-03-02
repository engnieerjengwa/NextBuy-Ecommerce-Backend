package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedAddressResponseDto {

    private Long id;
    private String label;
    private String street;
    private String city;
    private String province;
    private String country;
    private String zipCode;
    private String phoneNumber;
    private Boolean isDefault;
    private LocalDateTime dateCreated;
}
