package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.response.MessageResponseDto;

public interface NewsletterService {

    MessageResponseDto subscribe(String email);

    MessageResponseDto unsubscribe(String email);
}
