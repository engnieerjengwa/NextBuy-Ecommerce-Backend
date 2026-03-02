package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gift_card")
@Getter
@Setter
public class GiftCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "initial_amount", nullable = false)
    private BigDecimal initialAmount;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_id")
    private Customer purchaser;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "personal_message", columnDefinition = "TEXT")
    private String personalMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GiftCardStatus status = GiftCardStatus.ACTIVE;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    public enum GiftCardStatus {
        ACTIVE, REDEEMED, EXPIRED, CANCELLED
    }
}
