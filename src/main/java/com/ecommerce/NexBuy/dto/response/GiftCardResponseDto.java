package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GiftCardResponseDto {
    private Long id;
    private String code;
    private BigDecimal initialAmount;
    private BigDecimal remainingAmount;
    private String currency;
    private String recipientEmail;
    private String personalMessage;
    private String status;
    private LocalDate expiryDate;
    private LocalDateTime dateCreated;
}
