package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.response.RecentlyViewedProductDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.RecentlyViewedService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Recently Viewed", description = "Recently viewed product tracking")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedController {

    private final RecentlyViewedService recentlyViewedService;

    public RecentlyViewedController(RecentlyViewedService recentlyViewedService) {
        this.recentlyViewedService = recentlyViewedService;
    }

    @Operation(summary = "Track product view", description = "Record that the user viewed a product")
    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> trackProductView(@PathVariable Long productId,
                                                  Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        recentlyViewedService.trackProductView(userDetails.getEmail(), productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get recently viewed", description = "Retrieve the user's recently viewed products")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RecentlyViewedProductDto>> getRecentlyViewed(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<RecentlyViewedProductDto> products = recentlyViewedService.getRecentlyViewedProducts(userDetails.getEmail());
        return ResponseEntity.ok(products);
    }
}
