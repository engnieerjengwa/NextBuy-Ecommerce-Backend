package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.entity.HeroBanner;
import com.ecommerce.NexBuy.service.HeroBannerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing hero banners
 */
@Tag(name = "Hero Banners", description = "Homepage hero banner management")
@RestController
@RequestMapping("/api/hero-banners")
public class HeroBannerController {

    private final HeroBannerService heroBannerService;

    public HeroBannerController(HeroBannerService heroBannerService) {
        this.heroBannerService = heroBannerService;
    }

    @Operation(summary = "Get all banners", description = "Retrieve all hero banners")
    @GetMapping
    public ResponseEntity<List<HeroBanner>> getAllHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getAllHeroBanners());
    }

    @Operation(summary = "Get banner by ID", description = "Retrieve a specific hero banner")
    @GetMapping("/{id}")
    public ResponseEntity<HeroBanner> getHeroBannerById(@PathVariable Long id) {
        return heroBannerService.getHeroBannerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get active banners", description = "Retrieve all banners marked as active")
    @GetMapping("/active")
    public ResponseEntity<List<HeroBanner>> getActiveHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getActiveHeroBanners());
    }

    @Operation(summary = "Get current banners", description = "Retrieve banners currently active based on date range")
    @GetMapping("/current")
    public ResponseEntity<List<HeroBanner>> getCurrentlyActiveHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getCurrentlyActiveHeroBanners());
    }

    @Operation(summary = "Create banner", description = "Create a new hero banner")
    @PostMapping
    public ResponseEntity<HeroBanner> createHeroBanner(@RequestBody HeroBanner heroBanner) {
        HeroBanner createdBanner = heroBannerService.createHeroBanner(heroBanner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
    }

    @Operation(summary = "Update banner", description = "Update an existing hero banner")
    @PutMapping("/{id}")
    public ResponseEntity<HeroBanner> updateHeroBanner(@PathVariable Long id, @RequestBody HeroBanner heroBanner) {
        try {
            HeroBanner updatedBanner = heroBannerService.updateHeroBanner(id, heroBanner);
            return ResponseEntity.ok(updatedBanner);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete banner", description = "Delete a hero banner")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeroBanner(@PathVariable Long id) {
        try {
            heroBannerService.deleteHeroBanner(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update banner order", description = "Reorder hero banners by providing sorted IDs")
    @PutMapping("/order")
    public ResponseEntity<Void> updateHeroBannerOrder(@RequestBody List<Long> heroBannerIds) {
        try {
            heroBannerService.updateHeroBannerOrder(heroBannerIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Toggle banner active", description = "Toggle the active status of a hero banner")
    @PutMapping("/{id}/toggle-active")
    public ResponseEntity<HeroBanner> toggleHeroBannerActive(@PathVariable Long id) {
        try {
            HeroBanner updatedBanner = heroBannerService.toggleHeroBannerActive(id);
            return ResponseEntity.ok(updatedBanner);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
