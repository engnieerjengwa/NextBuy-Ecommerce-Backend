-- -----------------------------------------------------
-- V6: Add unique constraint to customer email
-- -----------------------------------------------------
-- This migration adds a unique constraint to the customer email field to ensure email addresses are unique

USE `full-stack-ecommerce`;

-- Add unique constraint to customer email
ALTER TABLE `customer` 
MODIFY `email` VARCHAR(255) NULL,
ADD CONSTRAINT `uk_customer_email` UNIQUE (`email`);