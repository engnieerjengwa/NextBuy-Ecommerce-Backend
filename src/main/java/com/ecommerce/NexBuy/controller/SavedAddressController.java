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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Addresses", description = "Saved delivery addresses management")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/addresses")
@PreAuthorize("isAuthenticated()")
public class SavedAddressController {

    private final SavedAddressService savedAddressService;

    public SavedAddressController(SavedAddressService savedAddressService) {
        this.savedAddressService = savedAddressService;
    }

    @Operation(summary = "Get addresses", description = "Retrieve all saved addresses for the authenticated customer")
    @GetMapping
    public ResponseEntity<List<SavedAddressResponseDto>> getAddresses(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<SavedAddressResponseDto> addresses = savedAddressService.getAddresses(userDetails.getEmail());
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Get address", description = "Retrieve a specific saved address by ID")
    @GetMapping("/{addressId}")
    public ResponseEntity<SavedAddressResponseDto> getAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.getAddress(userDetails.getEmail(), addressId);
        return ResponseEntity.ok(address);
    }

    @Operation(summary = "Create address", description = "Save a new delivery address")
    @PostMapping
    public ResponseEntity<SavedAddressResponseDto> createAddress(
            @Valid @RequestBody SavedAddressRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.createAddress(userDetails.getEmail(), requestDto);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @Operation(summary = "Update address", description = "Update an existing saved address")
    @PutMapping("/{addressId}")
    public ResponseEntity<SavedAddressResponseDto> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody SavedAddressRequestDto requestDto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.updateAddress(userDetails.getEmail(), addressId, requestDto);
        return ResponseEntity.ok(address);
    }

    @Operation(summary = "Delete address", description = "Remove a saved address")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        savedAddressService.deleteAddress(userDetails.getEmail(), addressId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Set default address", description = "Mark an address as the default delivery address")
    @PutMapping("/{addressId}/default")
    public ResponseEntity<SavedAddressResponseDto> setDefaultAddress(
            @PathVariable Long addressId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        SavedAddressResponseDto address = savedAddressService.setDefault(userDetails.getEmail(), addressId);
        return ResponseEntity.ok(address);
    }
}
