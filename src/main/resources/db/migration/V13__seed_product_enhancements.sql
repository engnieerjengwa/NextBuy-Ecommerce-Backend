-- V13__seed_product_enhancements.sql
-- Phase 2: Update existing 100 products with brand, original price, and images

-- Books (category_id = 1, product IDs 1-25)
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.2, review_count = 45 WHERE id = 1;
UPDATE product SET brand = 'HarperCollins', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 4.5, review_count = 120 WHERE id = 2;
UPDATE product SET brand = 'Simon & Schuster', original_price = unit_price * 1.25, discount_percentage = 20, is_new = TRUE, average_rating = 4.0, review_count = 30 WHERE id = 3;
UPDATE product SET brand = 'Random House', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.8, review_count = 67 WHERE id = 4;
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.7, review_count = 200 WHERE id = 5;
UPDATE product SET brand = 'HarperCollins', original_price = unit_price * 1.18, discount_percentage = 15, is_new = TRUE, average_rating = 4.1, review_count = 55 WHERE id = 6;
UPDATE product SET brand = 'Macmillan', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 3.9, review_count = 42 WHERE id = 7;
UPDATE product SET brand = 'Random House', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 4.3, review_count = 88 WHERE id = 8;
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 4.6, review_count = 150 WHERE id = 9;
UPDATE product SET brand = 'Simon & Schuster', original_price = unit_price * 1.28, discount_percentage = 22, is_new = TRUE, average_rating = 4.4, review_count = 75 WHERE id = 10;
UPDATE product SET brand = 'Macmillan', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.7, review_count = 28 WHERE id = 11;
UPDATE product SET brand = 'HarperCollins', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.0, review_count = 63 WHERE id = 12;
UPDATE product SET brand = 'Random House', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.2, review_count = 91 WHERE id = 13;
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.8, review_count = 210 WHERE id = 14;
UPDATE product SET brand = 'Simon & Schuster', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 3.5, review_count = 19 WHERE id = 15;
UPDATE product SET brand = 'Macmillan', original_price = unit_price * 1.18, discount_percentage = 15, is_new = FALSE, average_rating = 4.1, review_count = 52 WHERE id = 16;
UPDATE product SET brand = 'HarperCollins', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 4.3, review_count = 77 WHERE id = 17;
UPDATE product SET brand = 'Random House', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 3.9, review_count = 38 WHERE id = 18;
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.5, review_count = 130 WHERE id = 19;
UPDATE product SET brand = 'Simon & Schuster', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 4.0, review_count = 48 WHERE id = 20;
UPDATE product SET brand = 'Macmillan', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.2, review_count = 85 WHERE id = 21;
UPDATE product SET brand = 'HarperCollins', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.6, review_count = 22 WHERE id = 22;
UPDATE product SET brand = 'Random House', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.4, review_count = 96 WHERE id = 23;
UPDATE product SET brand = 'Penguin Books', original_price = unit_price * 1.28, discount_percentage = 22, is_new = TRUE, average_rating = 4.7, review_count = 180 WHERE id = 24;
UPDATE product SET brand = 'Simon & Schuster', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 4.1, review_count = 61 WHERE id = 25;

-- Coffee Mugs (category_id = 2, product IDs 26-50)
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.3, review_count = 58 WHERE id = 26;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.6, review_count = 140 WHERE id = 27;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.0, review_count = 35 WHERE id = 28;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.8, review_count = 72 WHERE id = 29;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.5, review_count = 110 WHERE id = 30;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.18, discount_percentage = 15, is_new = TRUE, average_rating = 4.2, review_count = 80 WHERE id = 31;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.9, review_count = 44 WHERE id = 32;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.4, review_count = 95 WHERE id = 33;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.1, review_count = 66 WHERE id = 34;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.7, review_count = 160 WHERE id = 35;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 3.7, review_count = 25 WHERE id = 36;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.0, review_count = 53 WHERE id = 37;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.3, review_count = 87 WHERE id = 38;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 4.5, review_count = 125 WHERE id = 39;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.2, review_count = 78 WHERE id = 40;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.8, review_count = 41 WHERE id = 41;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.6, review_count = 145 WHERE id = 42;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.0, review_count = 50 WHERE id = 43;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.18, discount_percentage = 15, is_new = FALSE, average_rating = 4.1, review_count = 69 WHERE id = 44;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.4, review_count = 100 WHERE id = 45;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 3.9, review_count = 37 WHERE id = 46;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.2, review_count = 82 WHERE id = 47;
UPDATE product SET brand = 'MugLife', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.5, review_count = 115 WHERE id = 48;
UPDATE product SET brand = 'HomeStyle', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.3, review_count = 90 WHERE id = 49;
UPDATE product SET brand = 'Ceramic Arts', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 3.6, review_count = 20 WHERE id = 50;

-- Mouse Pads (category_id = 3, product IDs 51-75)
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.1, review_count = 55 WHERE id = 51;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.5, review_count = 130 WHERE id = 52;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.0, review_count = 43 WHERE id = 53;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.8, review_count = 65 WHERE id = 54;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.6, review_count = 155 WHERE id = 55;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.18, discount_percentage = 15, is_new = TRUE, average_rating = 4.3, review_count = 85 WHERE id = 56;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.9, review_count = 40 WHERE id = 57;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.4, review_count = 98 WHERE id = 58;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.1, review_count = 60 WHERE id = 59;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.7, review_count = 170 WHERE id = 60;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 3.7, review_count = 27 WHERE id = 61;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.0, review_count = 50 WHERE id = 62;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.3, review_count = 83 WHERE id = 63;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 4.5, review_count = 120 WHERE id = 64;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.2, review_count = 76 WHERE id = 65;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.8, review_count = 39 WHERE id = 66;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.6, review_count = 148 WHERE id = 67;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.0, review_count = 47 WHERE id = 68;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.18, discount_percentage = 15, is_new = FALSE, average_rating = 4.1, review_count = 62 WHERE id = 69;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.4, review_count = 105 WHERE id = 70;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 3.9, review_count = 34 WHERE id = 71;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.2, review_count = 79 WHERE id = 72;
UPDATE product SET brand = 'GamePro', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.5, review_count = 118 WHERE id = 73;
UPDATE product SET brand = 'DeskMate', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.3, review_count = 88 WHERE id = 74;
UPDATE product SET brand = 'TechGear', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 3.6, review_count = 18 WHERE id = 75;

-- Luggage Tags (category_id = 4, product IDs 76-100)
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.2, review_count = 52 WHERE id = 76;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.5, review_count = 125 WHERE id = 77;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.0, review_count = 40 WHERE id = 78;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 3.8, review_count = 70 WHERE id = 79;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.6, review_count = 150 WHERE id = 80;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.18, discount_percentage = 15, is_new = TRUE, average_rating = 4.3, review_count = 82 WHERE id = 81;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.9, review_count = 46 WHERE id = 82;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.4, review_count = 93 WHERE id = 83;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.1, review_count = 58 WHERE id = 84;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.7, review_count = 165 WHERE id = 85;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 3.7, review_count = 24 WHERE id = 86;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.0, review_count = 56 WHERE id = 87;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.2, discount_percentage = 17, is_new = FALSE, average_rating = 4.3, review_count = 81 WHERE id = 88;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 4.5, review_count = 122 WHERE id = 89;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.15, discount_percentage = 13, is_new = TRUE, average_rating = 4.2, review_count = 74 WHERE id = 90;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.22, discount_percentage = 18, is_new = FALSE, average_rating = 3.8, review_count = 36 WHERE id = 91;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.1, discount_percentage = 9, is_new = FALSE, average_rating = 4.6, review_count = 142 WHERE id = 92;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.3, discount_percentage = 23, is_new = FALSE, average_rating = 4.0, review_count = 49 WHERE id = 93;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.18, discount_percentage = 15, is_new = FALSE, average_rating = 4.1, review_count = 64 WHERE id = 94;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.4, review_count = 102 WHERE id = 95;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.15, discount_percentage = 13, is_new = FALSE, average_rating = 3.9, review_count = 33 WHERE id = 96;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.25, discount_percentage = 20, is_new = FALSE, average_rating = 4.2, review_count = 77 WHERE id = 97;
UPDATE product SET brand = 'WanderTag', original_price = unit_price * 1.12, discount_percentage = 11, is_new = FALSE, average_rating = 4.5, review_count = 116 WHERE id = 98;
UPDATE product SET brand = 'JetSetter', original_price = unit_price * 1.2, discount_percentage = 17, is_new = TRUE, average_rating = 4.3, review_count = 86 WHERE id = 99;
UPDATE product SET brand = 'TravelPro', original_price = unit_price * 1.28, discount_percentage = 22, is_new = FALSE, average_rating = 3.6, review_count = 16 WHERE id = 100;

-- Insert 2 images for each product (using existing image_url as primary)
-- We'll generate secondary images using the same category images with different products
INSERT INTO product_image (product_id, image_url, alt_text, display_order, is_primary)
SELECT id, image_url, CONCAT(name, ' - Main Image'), 0, TRUE FROM product;

INSERT INTO product_image (product_id, image_url, alt_text, display_order, is_primary)
SELECT id, REPLACE(image_url, SUBSTRING_INDEX(image_url, '/', -1), CONCAT('alt-', SUBSTRING_INDEX(image_url, '/', -1))), CONCAT(name, ' - Alternate View'), 1, FALSE FROM product;
