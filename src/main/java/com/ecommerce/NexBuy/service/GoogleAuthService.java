package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.AuthResponseDto;

public interface GoogleAuthService {

    /**
     * Authenticate a user using a Google ID token.
     * If the user doesn't exist, create a new account.
     *
     * @param googleIdToken the Google ID token from the frontend
     * @return JWT auth response
     */
    AuthResponseDto authenticateWithGoogle(String googleIdToken);
}
