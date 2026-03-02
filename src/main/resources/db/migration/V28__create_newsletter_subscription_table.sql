-- Phase 4: BE-15 - Newsletter Subscription
CREATE TABLE newsletter_subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    subscribed_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
