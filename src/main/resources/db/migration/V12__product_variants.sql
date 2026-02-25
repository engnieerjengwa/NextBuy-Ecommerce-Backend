-- V12__product_variants.sql
-- Phase 2: Product variant system for sizes, colours, materials

CREATE TABLE product_variant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    variant_type VARCHAR(50) NOT NULL,
    variant_value VARCHAR(100) NOT NULL,
    sku_suffix VARCHAR(50),
    price_adjustment DECIMAL(10,2) DEFAULT 0,
    units_in_stock INT DEFAULT 0,
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE INDEX idx_product_variant_product_id ON product_variant(product_id);
CREATE INDEX idx_product_variant_type ON product_variant(variant_type);
