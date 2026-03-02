package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_answer")
@Getter
@Setter
@ToString(exclude = {"question"})
public class ProductAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ProductQuestion question;

    @Column(name = "answered_by_customer_id")
    private Long answeredByCustomerId;

    @Column(name = "answered_by_seller")
    private Boolean answeredBySeller = false;

    @Column(name = "answer", columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
