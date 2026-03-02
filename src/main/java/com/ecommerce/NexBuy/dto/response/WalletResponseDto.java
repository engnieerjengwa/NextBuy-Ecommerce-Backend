package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WalletResponseDto {
    private BigDecimal balance;
    private String currency;
    private LocalDateTime lastUpdated;
    private List<WalletTransactionDto> recentTransactions;

    @Data
    public static class WalletTransactionDto {
        private Long id;
        private BigDecimal amount;
        private String type;
        private String source;
        private String referenceId;
        private String description;
        private LocalDateTime dateCreated;
    }
}
