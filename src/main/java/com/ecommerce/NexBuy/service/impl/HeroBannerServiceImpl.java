package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.entity.HeroBanner;
import com.ecommerce.NexBuy.repo.HeroBannerRepository;
import com.ecommerce.NexBuy.service.HeroBannerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the HeroBannerService interface
 */
@Service
public class HeroBannerServiceImpl implements HeroBannerService {

    private final HeroBannerRepository heroBannerRepository;

    @Autowired
    public HeroBannerServiceImpl(HeroBannerRepository heroBannerRepository) {
        this.heroBannerRepository = heroBannerRepository;
    }

    @Override
    public List<HeroBanner> getAllHeroBanners() {
        return heroBannerRepository.findAll();
    }

    @Override
    public Optional<HeroBanner> getHeroBannerById(Long id) {
        return heroBannerRepository.findById(id);
    }

    @Override
    public List<HeroBanner> getActiveHeroBanners() {
        return heroBannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    @Override
    public List<HeroBanner> getCurrentlyActiveHeroBanners() {
        Date currentDate = new Date();
        return heroBannerRepository.findByIsActiveTrueAndStartDateBeforeAndEndDateAfterOrIsActiveTrueAndStartDateIsNullAndEndDateIsNullOrderByDisplayOrderAsc(
                currentDate, currentDate);
    }

    @Override
    @Transactional
    public HeroBanner createHeroBanner(HeroBanner heroBanner) {
        // Set default values if not provided
        if (heroBanner.getIsActive() == null) {
            heroBanner.setIsActive(true);
        }
        
        if (heroBanner.getDisplayOrder() == null) {
            heroBanner.setDisplayOrder(0);
        }
        
        return heroBannerRepository.save(heroBanner);
    }

    @Override
    @Transactional
    public HeroBanner updateHeroBanner(Long id, HeroBanner heroBanner) {
        HeroBanner existingBanner = heroBannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hero banner not found with id: " + id));
        
        // Update fields
        existingBanner.setTitle(heroBanner.getTitle());
        existingBanner.setSubtitle(heroBanner.getSubtitle());
        existingBanner.setCtaText(heroBanner.getCtaText());
        existingBanner.setCtaLink(heroBanner.getCtaLink());
        existingBanner.setImageUrl(heroBanner.getImageUrl());
        existingBanner.setVideoUrl(heroBanner.getVideoUrl());
        existingBanner.setDisclaimer(heroBanner.getDisclaimer());
        existingBanner.setIsActive(heroBanner.getIsActive());
        existingBanner.setStartDate(heroBanner.getStartDate());
        existingBanner.setEndDate(heroBanner.getEndDate());
        existingBanner.setDisplayOrder(heroBanner.getDisplayOrder());
        
        return heroBannerRepository.save(existingBanner);
    }

    @Override
    @Transactional
    public void deleteHeroBanner(Long id) {
        if (!heroBannerRepository.existsById(id)) {
            throw new EntityNotFoundException("Hero banner not found with id: " + id);
        }
        heroBannerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateHeroBannerOrder(List<Long> heroBannerIds) {
        for (int i = 0; i < heroBannerIds.size(); i++) {
            Long bannerId = heroBannerIds.get(i);
            HeroBanner banner = heroBannerRepository.findById(bannerId)
                    .orElseThrow(() -> new EntityNotFoundException("Hero banner not found with id: " + bannerId));
            
            banner.setDisplayOrder(i);
            heroBannerRepository.save(banner);
        }
    }

    @Override
    @Transactional
    public HeroBanner toggleHeroBannerActive(Long id) {
        HeroBanner banner = heroBannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hero banner not found with id: " + id));
        
        banner.setIsActive(!banner.getIsActive());
        return heroBannerRepository.save(banner);
    }
}