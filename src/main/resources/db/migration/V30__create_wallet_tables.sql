-- Phase 4: BE-22 - Store Credit / Wallet System
CREATE TABLE customer_wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE wallet_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    type ENUM('CREDIT', 'DEBIT') NOT NULL,
    source ENUM('REFUND', 'GIFT_CARD', 'LOYALTY_REWARD', 'MANUAL_ADJUSTMENT', 'REFERRAL') NOT NULL,
    reference_id VARCHAR(255),
    description VARCHAR(500),
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES customer_wallet(id)
);
