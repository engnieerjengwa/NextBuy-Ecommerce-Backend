package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.entity.HeroBanner;
import com.ecommerce.NexBuy.service.HeroBannerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing hero banners
 */
@RestController
@RequestMapping("/api/hero-banners")
public class HeroBannerController {

    private final HeroBannerService heroBannerService;

    public HeroBannerController(HeroBannerService heroBannerService) {
        this.heroBannerService = heroBannerService;
    }

    /**
     * Get all hero banners
     * @return List of all hero banners
     */
    @GetMapping
    public ResponseEntity<List<HeroBanner>> getAllHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getAllHeroBanners());
    }

    /**
     * Get a hero banner by ID
     * @param id The hero banner ID
     * @return The hero banner, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<HeroBanner> getHeroBannerById(@PathVariable Long id) {
        return heroBannerService.getHeroBannerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all active hero banners
     * @return List of active hero banners
     */
    @GetMapping("/active")
    public ResponseEntity<List<HeroBanner>> getActiveHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getActiveHeroBanners());
    }

    /**
     * Get all currently active hero banners (based on dates)
     * @return List of currently active hero banners
     */
    @GetMapping("/current")
    public ResponseEntity<List<HeroBanner>> getCurrentlyActiveHeroBanners() {
        return ResponseEntity.ok(heroBannerService.getCurrentlyActiveHeroBanners());
    }

    /**
     * Create a new hero banner
     * @param heroBanner The hero banner to create
     * @return The created hero banner
     */
    @PostMapping
    public ResponseEntity<HeroBanner> createHeroBanner(@RequestBody HeroBanner heroBanner) {
        HeroBanner createdBanner = heroBannerService.createHeroBanner(heroBanner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
    }

    /**
     * Update an existing hero banner
     * @param id The hero banner ID
     * @param heroBanner The updated hero banner data
     * @return The updated hero banner
     */
    @PutMapping("/{id}")
    public ResponseEntity<HeroBanner> updateHeroBanner(@PathVariable Long id, @RequestBody HeroBanner heroBanner) {
        try {
            HeroBanner updatedBanner = heroBannerService.updateHeroBanner(id, heroBanner);
            return ResponseEntity.ok(updatedBanner);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a hero banner
     * @param id The hero banner ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeroBanner(@PathVariable Long id) {
        try {
            heroBannerService.deleteHeroBanner(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update the display order of hero banners
     * @param heroBannerIds List of hero banner IDs in the desired order
     * @return No content if successful
     */
    @PutMapping("/order")
    public ResponseEntity<Void> updateHeroBannerOrder(@RequestBody List<Long> heroBannerIds) {
        try {
            heroBannerService.updateHeroBannerOrder(heroBannerIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Toggle the active status of a hero banner
     * @param id The hero banner ID
     * @return The updated hero banner
     */
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
