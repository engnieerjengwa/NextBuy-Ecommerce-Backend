package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Column(name="quantity")
    private int quantity;

    @Column(name="product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
}
