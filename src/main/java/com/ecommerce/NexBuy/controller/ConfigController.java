package com.ecommerce.NexBuy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Configuration", description = "Application configuration endpoints")
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.google.places.api-key}")
    private String googlePlacesApiKey;

    @Operation(summary = "Get Google Places API key", description = "Retrieve the API key for frontend address autocomplete")
    @GetMapping("/google-places-key")
    public ResponseEntity<Map<String, String>> getGooglePlacesApiKey() {
        return ResponseEntity.ok(Map.of("apiKey", googlePlacesApiKey));
    }
}
