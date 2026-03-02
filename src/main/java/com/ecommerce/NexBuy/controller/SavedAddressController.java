package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.SavedAddressRequestDto;
import com.ecommerce.NexBuy.dto.response.SavedAddressResponseDto;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.SavedAddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@PreAuthorize("isAuthenticated()")
public class SavedAddressController {

    private final SavedAddressService savedAddressService;

    public SavedAddressController(SavedAddressService savedAddressService) {
        this.savedAddressService = savedAddressService;
    }

    /**
     * Get all saved addresses for the authenticated customer
     */
    @GetMapping
    public ResponseEntity<List<SavedAddressResponseDto>> getAddresses(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<SavedAddressResponseDto> addresses = savedAddressService.getAddresses(userDetails.getEmail());
        return ResponseEntity.ok(addresses);
    }

    /**
     * Get a specific saved address
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<SavedAddressResponseDto> getAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.getAddress(userDetails.getEmail(), addressId);
        return ResponseEntity.ok(address);
    }

    /**
     * Save a new address
     */
    @PostMapping
    public ResponseEntity<SavedAddressResponseDto> createAddress(
            @Valid @RequestBody SavedAddressRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.createAddress(userDetails.getEmail(), requestDto);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    /**
     * Update an existing saved address
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<SavedAddressResponseDto> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody SavedAddressRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.updateAddress(userDetails.getEmail(), addressId, requestDto);
        return ResponseEntity.ok(address);
    }

    /**
     * Delete a saved address
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        savedAddressService.deleteAddress(userDetails.getEmail(), addressId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Set an address as the default
     */
    @PutMapping("/{addressId}/default")
    public ResponseEntity<SavedAddressResponseDto> setDefaultAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.setDefault(userDetails.getEmail(), addressId);
        return ResponseEntity.ok(address);
    }
}
