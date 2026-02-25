-- V11__product_enhancements.sql
-- Phase 2: Extend product with brand, pricing, specs, and multi-image support

ALTER TABLE product ADD COLUMN original_price DECIMAL(19,2) NULL;
ALTER TABLE product ADD COLUMN discount_percentage INT NULL;
ALTER TABLE product ADD COLUMN brand VARCHAR(255) NULL;
ALTER TABLE product ADD COLUMN specifications JSON NULL;
ALTER TABLE product ADD COLUMN weight_kg DECIMAL(6,2) NULL;
ALTER TABLE product ADD COLUMN length_cm DECIMAL(6,1) NULL;
ALTER TABLE product ADD COLUMN width_cm DECIMAL(6,1) NULL;
ALTER TABLE product ADD COLUMN height_cm DECIMAL(6,1) NULL;
ALTER TABLE product ADD COLUMN video_url VARCHAR(500) NULL;
ALTER TABLE product ADD COLUMN warranty_info VARCHAR(500) NULL;
ALTER TABLE product ADD COLUMN is_new BOOLEAN DEFAULT FALSE;
ALTER TABLE product ADD COLUMN average_rating DECIMAL(2,1) DEFAULT 0;
ALTER TABLE product ADD COLUMN review_count INT DEFAULT 0;

CREATE TABLE product_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    display_order INT DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE INDEX idx_product_image_product_id ON product_image(product_id);
CREATE INDEX idx_product_brand ON product(brand);
CREATE INDEX idx_product_is_new ON product(is_new);
CREATE INDEX idx_product_average_rating ON product(average_rating);
