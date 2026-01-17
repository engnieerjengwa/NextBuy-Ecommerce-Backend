-- Add new columns to orders table
ALTER TABLE orders
    ADD COLUMN delivery_date DATETIME,
    ADD COLUMN delivery_status VARCHAR(255),
    ADD COLUMN proof_of_delivery VARCHAR(255),
    ADD COLUMN is_returned BOOLEAN DEFAULT FALSE;

-- Create return_requests table
CREATE TABLE return_requests (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    return_reason VARCHAR(255),
    comments VARCHAR(1000),
    status VARCHAR(255) DEFAULT 'PENDING',
    date_created DATETIME(6),
    last_updated DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- Create return_items table
CREATE TABLE return_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    return_request_id BIGINT NOT NULL,
    order_item_id BIGINT NOT NULL,
    quantity INT,
    reason VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (return_request_id) REFERENCES return_requests (id),
    FOREIGN KEY (order_item_id) REFERENCES order_item (id)
);

-- Create indexes for better performance
CREATE INDEX idx_return_requests_order_id ON return_requests (order_id);
CREATE INDEX idx_return_items_return_request_id ON return_items (return_request_id);
CREATE INDEX idx_return_items_order_item_id ON return_items (order_item_id);