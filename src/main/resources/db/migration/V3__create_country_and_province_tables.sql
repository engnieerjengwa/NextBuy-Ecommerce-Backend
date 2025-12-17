-- -----------------------------------------------------
-- V3: Create country and province tables
-- -----------------------------------------------------
-- This migration creates the country and province tables with appropriate relationships

USE `full-stack-ecommerce`;

-- Create country table
CREATE TABLE IF NOT EXISTS `country` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(2) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create province table with foreign key to country
CREATE TABLE IF NOT EXISTS `province` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `country_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_country` (`country_id`),
  CONSTRAINT `fk_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
