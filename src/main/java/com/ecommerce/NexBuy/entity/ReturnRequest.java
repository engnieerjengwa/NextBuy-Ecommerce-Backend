package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "return_requests")
@Getter
@Setter
@ToString(exclude = {"order", "returnItems"})
@EqualsAndHashCode(exclude = {"order", "returnItems"})
public class ReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "return_reason")
    private String returnReason;

    @Column(name = "comments", length = 1000)
    private String comments;

    @Column(name = "status")
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, COMPLETED

    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReturnItem> returnItems = new HashSet<>();

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    public void addReturnItem(ReturnItem returnItem) {
        returnItems.add(returnItem);
        returnItem.setReturnRequest(this);
    }

    public void removeReturnItem(ReturnItem returnItem) {
        returnItems.remove(returnItem);
        returnItem.setReturnRequest(null);
    }
}
