-- Phase 3: BE-11 — Saved addresses (address book per customer)
CREATE TABLE saved_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    label VARCHAR(100),
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    province VARCHAR(255),
    country VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20),
    phone_number VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE INDEX idx_saved_address_customer_id ON saved_address(customer_id);
