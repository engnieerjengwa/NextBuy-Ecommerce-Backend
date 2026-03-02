package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.entity.HeroBanner;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing hero banners
 */
public interface HeroBannerService {
    
    /**
     * Get all hero banners
     * @return List of all hero banners
     */
    List<HeroBanner> getAllHeroBanners();
    
    /**
     * Get a hero banner by ID
     * @param id The hero banner ID
     * @return The hero banner, if found
     */
    Optional<HeroBanner> getHeroBannerById(Long id);
    
    /**
     * Get all active hero banners ordered by display order
     * @return List of active hero banners
     */
    List<HeroBanner> getActiveHeroBanners();
    
    /**
     * Get all active hero banners that are currently valid based on start and end dates
     * @return List of active and valid hero banners
     */
    List<HeroBanner> getCurrentlyActiveHeroBanners();
    
    /**
     * Create a new hero banner
     * @param heroBanner The hero banner to create
     * @return The created hero banner
     */
    HeroBanner createHeroBanner(HeroBanner heroBanner);
    
    /**
     * Update an existing hero banner
     * @param id The hero banner ID
     * @param heroBanner The updated hero banner data
     * @return The updated hero banner
     */
    HeroBanner updateHeroBanner(Long id, HeroBanner heroBanner);
    
    /**
     * Delete a hero banner
     * @param id The hero banner ID
     */
    void deleteHeroBanner(Long id);
    
    /**
     * Update the display order of hero banners
     * @param heroBannerIds List of hero banner IDs in the desired order
     */
    void updateHeroBannerOrder(List<Long> heroBannerIds);
    
    /**
     * Toggle the active status of a hero banner
     * @param id The hero banner ID
     * @return The updated hero banner
     */
    HeroBanner toggleHeroBannerActive(Long id);
}