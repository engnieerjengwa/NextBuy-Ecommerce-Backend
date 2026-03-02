package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "referral")
@Getter
@Setter
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", nullable = false)
    private Customer referrer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id")
    private Customer referee;

    @Column(name = "referral_code", nullable = false, unique = true, length = 50)
    private String referralCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status = ReferralStatus.PENDING;

    @Column(name = "reward_amount")
    private BigDecimal rewardAmount = new BigDecimal("5.00");

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_completed")
    private LocalDateTime dateCompleted;

    public enum ReferralStatus {
        PENDING, COMPLETED, EXPIRED
    }
}
