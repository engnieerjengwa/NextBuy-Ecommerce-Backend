package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transaction")
@Getter
@Setter
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private CustomerWallet wallet;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private TransactionSource source;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum TransactionSource {
        REFUND, GIFT_CARD, LOYALTY_REWARD, MANUAL_ADJUSTMENT, REFERRAL
    }
}
