-- Phase 4: BE-21 - Gift Card System
CREATE TABLE gift_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    initial_amount DECIMAL(19,2) NOT NULL,
    remaining_amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    purchaser_id BIGINT,
    recipient_email VARCHAR(255),
    personal_message TEXT,
    status ENUM('ACTIVE', 'REDEEMED', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
    expiry_date DATE,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (purchaser_id) REFERENCES customer(id)
);
