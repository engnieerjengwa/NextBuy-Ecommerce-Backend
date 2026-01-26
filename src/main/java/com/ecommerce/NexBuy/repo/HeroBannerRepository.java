package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.HeroBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "hero-banners", collectionResourceRel = "heroBanners")
@Repository
public interface HeroBannerRepository extends JpaRepository<HeroBanner, Long> {

    /**
     * Find all active hero banners ordered by display order
     * @return List of active hero banners
     */
    List<HeroBanner> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Find all active hero banners that are currently valid based on start and end dates
     * @param currentDate The current date to check against
     * @return List of active and valid hero banners
     */
    List<HeroBanner> findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrderByDisplayOrderAsc(Date currentDate, Date sameCurrentDate);
    
    /**
     * Find all active hero banners that are currently valid or have no date restrictions
     * @param currentDate The current date to check against
     * @return List of active and valid hero banners
     */
    List<HeroBanner> findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrIsActiveTrueAndStartDateIsNullAndEndDateIsNullOrderByDisplayOrderAsc(Date currentDate, Date sameCurrentDate);
    
    /**
     * Find hero banner by ID and active status
     * @param id The hero banner ID
     * @return The active hero banner if found
     */
    HeroBanner findByIdAndIsActiveTrue(Long id);
}