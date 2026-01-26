-- Create hero_banners table
CREATE TABLE hero_banners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    cta_text VARCHAR(100),
    cta_link VARCHAR(255),
    image_url VARCHAR(255) NOT NULL,
    video_url VARCHAR(255),
    disclaimer VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    start_date DATETIME,
    end_date DATETIME,
    display_order INT NOT NULL DEFAULT 0,
    date_created DATETIME NOT NULL,
    last_updated DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Add index for performance
CREATE INDEX idx_hero_banner_active ON hero_banners (is_active);
CREATE INDEX idx_hero_banner_dates ON hero_banners (start_date, end_date);
CREATE INDEX idx_hero_banner_order ON hero_banners (display_order);

-- Insert a sample hero banner
INSERT INTO hero_banners (
    title, 
    subtitle, 
    cta_text, 
    cta_link, 
    image_url, 
    disclaimer, 
    is_active, 
    display_order, 
    date_created, 
    last_updated
) VALUES (
    'Summer Sale', 
    'Up to 50% Off', 
    'Shop Now', 
    '/category/1/Summer', 
    'assets/images/banners/summer-sale.jpg', 
    'Limited time offer on selected items. Terms and conditions apply.', 
    TRUE, 
    0, 
    NOW(), 
    NOW()
);