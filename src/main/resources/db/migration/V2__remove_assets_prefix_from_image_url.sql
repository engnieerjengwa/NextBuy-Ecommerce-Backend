-- -----------------------------------------------------
-- V3: Remove leading `assets/` from product.image_url
-- -----------------------------------------------------
-- This migration strips the prefix `assets/` from any image_url that starts with it.

USE `full-stack-ecommerce`;

UPDATE `product`
SET `image_url` = SUBSTRING(`image_url`, CHAR_LENGTH('assets/') + 1)
WHERE `image_url` LIKE 'assets/%';

