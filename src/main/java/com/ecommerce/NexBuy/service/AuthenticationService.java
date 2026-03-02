package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.LoginRequestDto;
import com.ecommerce.NexBuy.dto.request.RegisterRequestDto;
import com.ecommerce.NexBuy.dto.response.AuthResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;

public interface AuthenticationService {

    AuthResponseDto login(LoginRequestDto loginRequest);

    MessageResponseDto register(RegisterRequestDto registerRequest);

    AuthResponseDto refreshToken(String refreshToken);
}
