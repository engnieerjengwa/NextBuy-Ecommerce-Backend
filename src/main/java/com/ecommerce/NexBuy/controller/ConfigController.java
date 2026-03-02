package com.ecommerce.NexBuy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.google.places.api-key}")
    private String googlePlacesApiKey;

    /**
     * Expose the Google Places API key for frontend address autocomplete
     */
    @GetMapping("/google-places-key")
    public ResponseEntity<Map<String, String>> getGooglePlacesApiKey() {
        return ResponseEntity.ok(Map.of("apiKey", googlePlacesApiKey));
    }
}
