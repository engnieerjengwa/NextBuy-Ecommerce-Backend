-- Phase 3: BE-26 — Back-in-stock notifications
CREATE TABLE stock_notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    is_notified BOOLEAN DEFAULT FALSE,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_notified DATETIME,
    FOREIGN KEY (product_id) REFERENCES product(id),
    UNIQUE(product_id, customer_email)
);

CREATE INDEX idx_stock_notification_product_id ON stock_notification(product_id);
