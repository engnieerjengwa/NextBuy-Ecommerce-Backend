package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, Long> {
    Optional<NewsletterSubscription> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
