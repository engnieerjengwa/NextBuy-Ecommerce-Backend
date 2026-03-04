package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.entity.NewsletterSubscription;
import com.ecommerce.NexBuy.repo.NewsletterSubscriptionRepository;
import com.ecommerce.NexBuy.service.NewsletterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private static final Logger logger = LoggerFactory.getLogger(NewsletterServiceImpl.class);

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    public NewsletterServiceImpl(NewsletterSubscriptionRepository newsletterSubscriptionRepository) {
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
    }

    @Override
    @Transactional
    public MessageResponseDto subscribe(String email) {
        Optional<NewsletterSubscription> existing = newsletterSubscriptionRepository.findByEmailIgnoreCase(email);

        if (existing.isPresent()) {
            NewsletterSubscription subscription = existing.get();
            if (subscription.getIsActive()) {
                return new MessageResponseDto("You are already subscribed to our newsletter.");
            }
            // Re-activate
            subscription.setIsActive(true);
            subscription.setSubscribedAt(LocalDateTime.now());
            newsletterSubscriptionRepository.save(subscription);
            logger.info("Newsletter re-subscription for: {}", email);
            return new MessageResponseDto("Welcome back! You have been re-subscribed to our newsletter.");
        }

        NewsletterSubscription subscription = new NewsletterSubscription();
        subscription.setEmail(email.toLowerCase());
        subscription.setIsActive(true);
        subscription.setSubscribedAt(LocalDateTime.now());
        newsletterSubscriptionRepository.save(subscription);

        logger.info("New newsletter subscription for: {}", email);
        return new MessageResponseDto("Thank you for subscribing to our newsletter!");
    }

    @Override
    @Transactional
    public MessageResponseDto unsubscribe(String email) {
        Optional<NewsletterSubscription> existing = newsletterSubscriptionRepository.findByEmailIgnoreCase(email);

        if (existing.isEmpty() || !existing.get().getIsActive()) {
            return new MessageResponseDto("This email is not currently subscribed.");
        }

        existing.get().setIsActive(false);
        newsletterSubscriptionRepository.save(existing.get());

        logger.info("Newsletter unsubscription for: {}", email);
        return new MessageResponseDto("You have been unsubscribed from our newsletter.");
    }
}
