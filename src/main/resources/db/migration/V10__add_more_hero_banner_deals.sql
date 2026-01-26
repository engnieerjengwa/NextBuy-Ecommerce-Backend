-- Add more hero banner deals for testing

-- Winter Sale Banner
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
    'Winter Sale', 
    'Up to 70% Off Winter Essentials', 
    'Shop Winter', 
    '/category/2/Winter', 
    'assets/images/banners/winter-sale.jpg', 
    'Limited time offer on selected winter items. While supplies last.', 
    TRUE, 
    1, 
    NOW(), 
    NOW()
);

-- Electronics Deal Banner
INSERT INTO hero_banners (
    title, 
    subtitle, 
    cta_text, 
    cta_link, 
    image_url, 
    disclaimer, 
    is_active, 
    start_date,
    end_date,
    display_order, 
    date_created, 
    last_updated
) VALUES (
    'Tech Bonanza', 
    'Latest Electronics at Unbeatable Prices', 
    'Explore Tech', 
    '/category/3/Electronics', 
    'assets/images/banners/electronics-deal.jpg', 
    'Prices valid for limited time. Warranty terms apply.', 
    TRUE, 
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    DATE_ADD(NOW(), INTERVAL 30 DAY),
    2, 
    NOW(), 
    NOW()
);

-- Fashion Deal Banner
INSERT INTO hero_banners (
    title, 
    subtitle, 
    cta_text, 
    cta_link, 
    image_url, 
    disclaimer, 
    is_active, 
    start_date,
    end_date,
    display_order, 
    date_created, 
    last_updated
) VALUES (
    'Fashion Forward', 
    'New Season Styles Just Arrived', 
    'Shop Fashion', 
    '/category/4/Fashion', 
    'assets/images/banners/fashion-deal.jpg', 
    'Selected styles only. See website for details.', 
    TRUE, 
    DATE_SUB(NOW(), INTERVAL 2 DAY),
    DATE_ADD(NOW(), INTERVAL 15 DAY),
    3, 
    NOW(), 
    NOW()
);

-- Home & Garden Banner
INSERT INTO hero_banners (
    title, 
    subtitle, 
    cta_text, 
    cta_link, 
    image_url, 
    video_url,
    disclaimer, 
    is_active, 
    display_order, 
    date_created, 
    last_updated
) VALUES (
    'Home Makeover', 
    'Transform Your Space with 30% Off', 
    'Shop Home', 
    '/category/5/Home', 
    'assets/images/banners/home-garden.jpg', 
    'assets/videos/home-makeover.mp4',
    'Discount applies to selected home items only.', 
    TRUE, 
    4, 
    NOW(), 
    NOW()
);

-- Future Banner (not yet active)
INSERT INTO hero_banners (
    title, 
    subtitle, 
    cta_text, 
    cta_link, 
    image_url, 
    disclaimer, 
    is_active, 
    start_date,
    end_date,
    display_order, 
    date_created, 
    last_updated
) VALUES (
    'Coming Soon: Spring Collection', 
    'Be the First to Shop Our New Arrivals', 
    'Get Notified', 
    '/notify/spring', 
    'assets/images/banners/spring-preview.jpg', 
    'Sign up to be notified when our Spring Collection launches.', 
    TRUE, 
    DATE_ADD(NOW(), INTERVAL 30 DAY),
    DATE_ADD(NOW(), INTERVAL 90 DAY),
    5, 
    NOW(), 
    NOW()
);

-- Inactive Banner (for testing toggle functionality)
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
    'Flash Sale', 
    '24 Hours Only - 50% Off Everything', 
    'Shop Now', 
    '/flash-sale', 
    'assets/images/banners/flash-sale.jpg', 
    'Flash sale valid for 24 hours only. No rain checks.', 
    FALSE, 
    6, 
    NOW(), 
    NOW()
);