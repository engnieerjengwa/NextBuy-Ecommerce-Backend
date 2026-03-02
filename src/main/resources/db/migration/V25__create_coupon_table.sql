-- Phase 4: BE-10 - Coupon/Voucher System
CREATE TABLE coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order_amount DECIMAL(19,2),
    max_uses INT,
    current_uses INT DEFAULT 0,
    start_date DATETIME,
    end_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Seed sample coupons
INSERT INTO coupon (code, description, discount_type, discount_value, minimum_order_amount, max_uses, current_uses, start_date, end_date, is_active)
VALUES
('WELCOME10', 'Welcome discount - 10% off your first order', 'PERCENTAGE', 10.00, 50.00, 1000, 0, '2026-01-01 00:00:00', '2026-12-31 23:59:59', TRUE),
('SAVE20', 'Save $20 on orders over $100', 'FIXED_AMOUNT', 20.00, 100.00, 500, 0, '2026-01-01 00:00:00', '2026-06-30 23:59:59', TRUE),
('FLASH15', 'Flash sale - 15% off everything', 'PERCENTAGE', 15.00, NULL, 200, 0, '2026-03-01 00:00:00', '2026-03-31 23:59:59', TRUE),
('FREEBIE5', '$5 off any order', 'FIXED_AMOUNT', 5.00, 25.00, NULL, 0, '2026-01-01 00:00:00', '2026-12-31 23:59:59', TRUE);
