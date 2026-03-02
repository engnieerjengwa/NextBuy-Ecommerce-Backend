package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    // Phase 2: Extended product fields
    @Column(name = "original_price")
    private BigDecimal originalPrice;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "brand")
    private String brand;

    @Column(name = "specifications", columnDefinition = "JSON")
    private String specifications;

    @Column(name = "weight_kg")
    private BigDecimal weightKg;

    @Column(name = "length_cm")
    private BigDecimal lengthCm;

    @Column(name = "width_cm")
    private BigDecimal widthCm;

    @Column(name = "height_cm")
    private BigDecimal heightCm;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "warranty_info")
    private String warrantyInfo;

    @Column(name = "is_new")
    private Boolean isNew = false;

    @Column(name = "average_rating")
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @ToString.Exclude
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ProductVariant> variants = new ArrayList<>();

    // Phase 4: Pre-order fields (BE-37)
    @Column(name = "is_preorder")
    private Boolean isPreorder = false;

    @Column(name = "preorder_release_date")
    private java.time.LocalDate preorderReleaseDate;

    @Column(name = "preorder_message", length = 500)
    private String preorderMessage;
}
