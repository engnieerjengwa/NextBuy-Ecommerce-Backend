package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.GoogleAuthRequestDto;
import com.ecommerce.NexBuy.dto.request.LoginRequestDto;
import com.ecommerce.NexBuy.dto.request.RegisterRequestDto;
import com.ecommerce.NexBuy.dto.response.AuthResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.service.AuthenticationService;
import com.ecommerce.NexBuy.service.GoogleAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@Tag(name = "Authentication", description = "User registration, login, and token management")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final GoogleAuthService googleAuthService;

    @Operation(summary = "Login", description = "Authenticate user and return JWT access + refresh tokens")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto authResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register", description = "Create a new customer account")
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        MessageResponseDto response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Google OAuth login", description = "Authenticate with a Google ID token")
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDto> googleLogin(@Valid @RequestBody GoogleAuthRequestDto request) {
        AuthResponseDto authResponse = googleAuthService.authenticateWithGoogle(request.getCredential());
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Refresh token", description = "Exchange a refresh token for a new access token")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        AuthResponseDto authResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Get current user", description = "Retrieve the authenticated user's profile information")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        var userDetails = (com.ecommerce.NexBuy.security.UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> userInfo = Map.of(
                "id", userDetails.getId(),
                "email", userDetails.getEmail(),
                "firstName", userDetails.getFirstName(),
                "lastName", userDetails.getLastName(),
                "roles", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList()
        );
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "Register privileged user", description = "Admin-only: create a seller or admin account")
    @PostMapping("/register-privileged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponseDto> registerPrivileged(@Valid @RequestBody RegisterRequestDto registerRequest) {
        MessageResponseDto response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
