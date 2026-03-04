-- -----------------------------------------------------
-- V37: Add seller location fields to product table
-- -----------------------------------------------------
-- Adds seller_province and seller_city to support location-based delivery cost.
-- Defaults all existing products to 'Harare' (main warehouse location).

USE `full-stack-ecommerce`;

ALTER TABLE `product`
  ADD COLUMN `seller_province` VARCHAR(255) DEFAULT 'Harare' AFTER `preorder_message`,
  ADD COLUMN `seller_city`     VARCHAR(255) DEFAULT 'Harare' AFTER `seller_province`;
