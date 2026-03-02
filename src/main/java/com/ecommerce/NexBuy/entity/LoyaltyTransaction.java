package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_transaction")
@Getter
@Setter
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_id", nullable = false)
    private LoyaltyProgram loyaltyProgram;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LoyaltyTransactionType type;

    @Column(name = "source")
    private String source;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    public enum LoyaltyTransactionType {
        EARNED, REDEEMED, EXPIRED, BONUS
    }
}
