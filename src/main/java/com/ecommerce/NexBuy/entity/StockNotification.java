package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_notification", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "customer_email"})
})
@Getter
@Setter
public class StockNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "is_notified")
    private Boolean isNotified = false;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_notified")
    private LocalDateTime dateNotified;
}
