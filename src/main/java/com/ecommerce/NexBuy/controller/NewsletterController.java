package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.NewsletterSubscribeRequestDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.service.NewsletterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Newsletter", description = "Newsletter subscription management")
@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @Operation(summary = "Subscribe to newsletter", description = "Subscribe an email address to the newsletter")
    @PostMapping("/subscribe")
    public ResponseEntity<MessageResponseDto> subscribe(@Valid @RequestBody NewsletterSubscribeRequestDto request) {
        MessageResponseDto response = newsletterService.subscribe(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Unsubscribe from newsletter", description = "Unsubscribe an email address from the newsletter")
    @PostMapping("/unsubscribe")
    public ResponseEntity<MessageResponseDto> unsubscribe(@Valid @RequestBody NewsletterSubscribeRequestDto request) {
        MessageResponseDto response = newsletterService.unsubscribe(request.getEmail());
        return ResponseEntity.ok(response);
    }
}
