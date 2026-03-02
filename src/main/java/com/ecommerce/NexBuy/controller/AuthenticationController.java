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

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final GoogleAuthService googleAuthService;

    /**
     * Login endpoint - authenticates user and returns JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto authResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Register endpoint - creates new user account
     * Default role is CUSTOMER if not specified
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        MessageResponseDto response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Google OAuth login endpoint - authenticates with Google ID token
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDto> googleLogin(@Valid @RequestBody GoogleAuthRequestDto request) {
        AuthResponseDto authResponse = googleAuthService.authenticateWithGoogle(request.getCredential());
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Refresh token endpoint - exchanges refresh token for new access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        AuthResponseDto authResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Get current user info - requires authentication
     */
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

    /**
     * Admin-only: Register a seller or admin account
     */
    @PostMapping("/register-privileged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponseDto> registerPrivileged(@Valid @RequestBody RegisterRequestDto registerRequest) {
        MessageResponseDto response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
