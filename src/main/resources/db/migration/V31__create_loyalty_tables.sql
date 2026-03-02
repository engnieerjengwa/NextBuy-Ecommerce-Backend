-- Phase 4: BE-23 - Loyalty / Rewards Program
CREATE TABLE loyalty_program (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL UNIQUE,
    tier ENUM('BRONZE', 'SILVER', 'GOLD', 'PLATINUM') NOT NULL DEFAULT 'BRONZE',
    total_points INT NOT NULL DEFAULT 0,
    lifetime_points INT NOT NULL DEFAULT 0,
    tier_expiry_date DATE,
    date_joined DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE loyalty_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loyalty_id BIGINT NOT NULL,
    points INT NOT NULL,
    type ENUM('EARNED', 'REDEEMED', 'EXPIRED', 'BONUS') NOT NULL,
    source VARCHAR(255),
    order_id BIGINT,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (loyalty_id) REFERENCES loyalty_program(id)
);
