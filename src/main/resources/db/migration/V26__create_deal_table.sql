-- Phase 4: BE-18 - Deals / Flash Sales
CREATE TABLE deal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    deal_price DECIMAL(19,2) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    max_quantity INT,
    sold_quantity INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    deal_type ENUM('DAILY_DEAL', 'FLASH_SALE', 'PROMOTION') NOT NULL DEFAULT 'DAILY_DEAL',
    title VARCHAR(255),
    description VARCHAR(500),
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Seed sample deals
INSERT INTO deal (product_id, deal_price, start_time, end_time, max_quantity, sold_quantity, is_active, deal_type, title, description)
VALUES
(1, 799.99, '2026-03-01 00:00:00', '2026-03-07 23:59:59', 50, 0, TRUE, 'DAILY_DEAL', 'Tech Week Special', 'Amazing deals on electronics'),
(2, 49.99, '2026-03-02 08:00:00', '2026-03-02 20:00:00', 100, 0, TRUE, 'FLASH_SALE', 'Lightning Deal', '12-hour flash sale'),
(3, 29.99, '2026-03-01 00:00:00', '2026-03-31 23:59:59', NULL, 0, TRUE, 'PROMOTION', 'March Madness', 'All month long savings');
