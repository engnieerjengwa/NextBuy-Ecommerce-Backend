-- Phase 4: BE-24 - Referral Program
CREATE TABLE referral (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    referrer_id BIGINT NOT NULL,
    referee_id BIGINT,
    referral_code VARCHAR(50) NOT NULL UNIQUE,
    status ENUM('PENDING', 'COMPLETED', 'EXPIRED') NOT NULL DEFAULT 'PENDING',
    reward_amount DECIMAL(10,2) DEFAULT 5.00,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_completed DATETIME,
    FOREIGN KEY (referrer_id) REFERENCES customer(id)
);
