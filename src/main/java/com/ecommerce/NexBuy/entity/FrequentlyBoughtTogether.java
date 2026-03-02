package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "frequently_bought_together",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "related_product_id"}))
@Getter
@Setter
public class FrequentlyBoughtTogether {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "related_product_id", nullable = false)
    private Product relatedProduct;

    @Column(name = "co_purchase_count")
    private Integer coPurchaseCount = 0;

    @Column(name = "discount_percentage")
    private Integer discountPercentage = 0;
}
