package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variant")
@Data
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @Column(name = "variant_type", nullable = false)
    private String variantType;

    @Column(name = "variant_value", nullable = false)
    private String variantValue;

    @Column(name = "sku_suffix")
    private String skuSuffix;

    @Column(name = "price_adjustment")
    private BigDecimal priceAdjustment = BigDecimal.ZERO;

    @Column(name = "units_in_stock")
    private int unitsInStock = 0;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
