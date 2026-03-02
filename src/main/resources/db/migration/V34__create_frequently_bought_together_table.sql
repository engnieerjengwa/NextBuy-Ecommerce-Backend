-- Phase 4: BE-44 - Frequently Bought Together Engine
CREATE TABLE frequently_bought_together (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    related_product_id BIGINT NOT NULL,
    co_purchase_count INT DEFAULT 0,
    discount_percentage INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (related_product_id) REFERENCES product(id),
    UNIQUE(product_id, related_product_id)
);

-- Seed some sample frequently_bought_together data
INSERT INTO frequently_bought_together (product_id, related_product_id, co_purchase_count, discount_percentage)
VALUES
(1, 2, 45, 5),
(1, 3, 30, 0),
(2, 3, 25, 10),
(2, 1, 45, 5);
