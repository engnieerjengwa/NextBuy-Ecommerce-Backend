-- Phase 3: BE-5 — Wishlist system
CREATE TABLE wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(255) DEFAULT 'My Wishlist',
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE wishlist_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wishlist_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id),
    UNIQUE(wishlist_id, product_id)
);

CREATE INDEX idx_wishlist_customer_id ON wishlist(customer_id);
CREATE INDEX idx_wishlist_item_wishlist_id ON wishlist_item(wishlist_id);
