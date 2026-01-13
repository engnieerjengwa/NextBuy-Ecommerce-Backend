-- -----------------------------------------------------
-- V5: Create checkout-related tables
-- -----------------------------------------------------
-- This migration creates the customer, address, order, and order_item tables with appropriate relationships

USE `full-stack-ecommerce`;

-- Create customer table
CREATE TABLE IF NOT EXISTS `customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255)  NULL,
  `last_name` VARCHAR(255) NULL,
  `email` VARCHAR(255)  NULL,
  `mobile_number` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create address table
CREATE TABLE IF NOT EXISTS `address` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `street` VARCHAR(255) NULL,
  `city` VARCHAR(255) NULL,
  `state` VARCHAR(255)  NULL,
  `country` VARCHAR(255)  NULL,
  `zip_code` VARCHAR(255)  NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create order table
CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_tracking_number` VARCHAR(255)  NULL,
  `total_quantity` INT  NULL,
  `total_price` DECIMAL(19,2) NULL,
  `status` VARCHAR(128)  NULL,
  `date_created` DATETIME(6)  NULL,
  `last_updated` DATETIME(6)  NULL,
  `customer_id` BIGINT  NULL,
  `shipping_address_id` BIGINT NULL,
  `billing_address_id` BIGINT  NULL,
  PRIMARY KEY (`id`),
  KEY `fk_customer_id` (`customer_id`),
  KEY `fk_shipping_address_id` (`shipping_address_id`),
  KEY `fk_billing_address_id` (`billing_address_id`),
  CONSTRAINT `fk_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `fk_shipping_address_id` FOREIGN KEY (`shipping_address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_billing_address_id` FOREIGN KEY (`billing_address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create order_item table
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `image_url` VARCHAR(255),
  `unit_price` DECIMAL(19,2)  NULL,
  `quantity` INT NULL,
  `product_id` BIGINT  NULL,
  `order_id` BIGINT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_id` (`order_id`),
  CONSTRAINT `fk_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
