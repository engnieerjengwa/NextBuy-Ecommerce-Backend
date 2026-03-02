package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deal")
@Getter
@Setter
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "deal_price", nullable = false)
    private BigDecimal dealPrice;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    @Column(name = "sold_quantity")
    private Integer soldQuantity = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type", nullable = false)
    private DealType dealType = DealType.DAILY_DEAL;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    public enum DealType {
        DAILY_DEAL, FLASH_SALE, PROMOTION
    }

    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive
                && now.isAfter(startTime)
                && now.isBefore(endTime)
                && (maxQuantity == null || soldQuantity < maxQuantity);
    }
}
