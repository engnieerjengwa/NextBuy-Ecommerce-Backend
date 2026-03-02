package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loyalty_program")
@Getter
@Setter
public class LoyaltyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    private LoyaltyTier tier = LoyaltyTier.BRONZE;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "lifetime_points", nullable = false)
    private Integer lifetimePoints = 0;

    @Column(name = "tier_expiry_date")
    private LocalDate tierExpiryDate;

    @Column(name = "date_joined")
    @CreationTimestamp
    private LocalDateTime dateJoined;

    @OneToMany(mappedBy = "loyaltyProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoyaltyTransaction> transactions = new ArrayList<>();

    public enum LoyaltyTier {
        BRONZE(0), SILVER(500), GOLD(2000), PLATINUM(5000);

        private final int requiredPoints;

        LoyaltyTier(int requiredPoints) {
            this.requiredPoints = requiredPoints;
        }

        public int getRequiredPoints() {
            return requiredPoints;
        }

        public static LoyaltyTier fromPoints(int lifetimePoints) {
            if (lifetimePoints >= PLATINUM.requiredPoints) return PLATINUM;
            if (lifetimePoints >= GOLD.requiredPoints) return GOLD;
            if (lifetimePoints >= SILVER.requiredPoints) return SILVER;
            return BRONZE;
        }

        public LoyaltyTier nextTier() {
            return switch (this) {
                case BRONZE -> SILVER;
                case SILVER -> GOLD;
                case GOLD -> PLATINUM;
                case PLATINUM -> PLATINUM;
            };
        }
    }
}
