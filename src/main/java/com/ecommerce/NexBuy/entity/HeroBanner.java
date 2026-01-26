package com.ecommerce.NexBuy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Table(name = "hero_banners")
@Data
@EqualsAndHashCode(callSuper = true)
public class HeroBanner extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "cta_text")
    private String ctaText;

    @Column(name = "cta_link")
    private String ctaLink;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "disclaimer")
    private String disclaimer;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;
}