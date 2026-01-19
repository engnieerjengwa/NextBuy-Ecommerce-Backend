package com.ecommerce.NexBuy.dto.request;

import lombok.Data;

@Data
public class PaymentInfoRequestDto {
    private int amount;
    private String currency;
    private String receiptEmail;
}
