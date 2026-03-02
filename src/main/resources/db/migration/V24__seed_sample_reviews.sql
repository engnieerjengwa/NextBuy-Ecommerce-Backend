-- Phase 3: Seed sample reviews for demo purposes (10-20 reviews across products)
-- Uses customer_id = 1 (assumes at least one customer exists from checkout)

-- We'll insert reviews for products 1-10 with varied ratings
-- First, ensure a demo customer exists
INSERT IGNORE INTO customer (id, first_name, last_name, email) VALUES
(1, 'Demo', 'User', 'demo@nexbuy.com'),
(2, 'Jane', 'Smith', 'jane.smith@example.com'),
(3, 'John', 'Doe', 'john.doe@example.com');

-- Insert sample reviews
INSERT INTO review (product_id, customer_id, rating, title, comment, is_verified_purchase, helpful_count) VALUES
(1, 1, 5, 'Excellent product!', 'This exceeded my expectations. Great quality and fast delivery.', TRUE, 12),
(1, 2, 4, 'Very good', 'Good product overall. Minor packaging issues but product was fine.', TRUE, 5),
(1, 3, 3, 'Decent', 'Average product. Does what it says but nothing special.', FALSE, 2),
(2, 1, 4, 'Great value for money', 'Really happy with this purchase. Would recommend to others.', TRUE, 8),
(2, 2, 5, 'Love it!', 'Absolutely fantastic! Best purchase I have made in a while.', TRUE, 15),
(3, 1, 5, 'Perfect!', 'Exactly what I was looking for. Perfect condition and quality.', TRUE, 10),
(3, 3, 4, 'Good quality', 'Solid product. Arrived on time and works as described.', TRUE, 3),
(4, 1, 3, 'It is okay', 'Not bad but not great either. Adequate for the price.', FALSE, 1),
(4, 2, 4, 'Nice product', 'Pretty good product. Happy with my purchase.', TRUE, 6),
(5, 1, 5, 'Amazing!', 'Top quality product. Will definitely buy again.', TRUE, 20),
(5, 2, 5, 'Five stars', 'Outstanding product and service. Highly recommended!', TRUE, 18),
(5, 3, 4, 'Impressed', 'Very impressive product. Minor improvement could be made to packaging.', FALSE, 4),
(6, 1, 4, 'Solid choice', 'Good quality and good price. Satisfied with my purchase.', TRUE, 7),
(7, 2, 3, 'Average', 'Met my basic expectations but nothing more.', TRUE, 2),
(7, 3, 5, 'Superb!', 'Way better than expected! Great product.', TRUE, 11),
(8, 1, 4, 'Recommended', 'Would definitely recommend this to friends and family.', TRUE, 9),
(9, 2, 5, 'Best purchase ever', 'I am extremely happy with this product. A must-have!', TRUE, 14),
(10, 1, 3, 'Fair enough', 'Reasonable product for the price. Does the job.', FALSE, 1),
(10, 3, 4, 'Good product', 'Well made and arrived quickly. Good experience overall.', TRUE, 5);

-- Update product average_rating and review_count based on seeded reviews
UPDATE product p
SET p.average_rating = (
    SELECT ROUND(AVG(r.rating), 1)
    FROM review r
    WHERE r.product_id = p.id
),
p.review_count = (
    SELECT COUNT(*)
    FROM review r
    WHERE r.product_id = p.id
)
WHERE p.id IN (SELECT DISTINCT product_id FROM review);
