-- Update product_category table to match the ProductCategory entity
ALTER TABLE `full-stack-ecommerce`.`product_category` 
ADD COLUMN `slug` VARCHAR(255) UNIQUE NULL,
ADD COLUMN `icon_url` VARCHAR(255) NULL,
ADD COLUMN `is_active` BIT DEFAULT 1,
ADD COLUMN `display_order` INT DEFAULT 0,
ADD COLUMN `parent_id` BIGINT(20) NULL;

-- Add foreign key constraint for parent_id
ALTER TABLE `full-stack-ecommerce`.`product_category` 
ADD CONSTRAINT `fk_parent_category` 
FOREIGN KEY (`parent_id`) 
REFERENCES `full-stack-ecommerce`.`product_category` (`id`);

-- Update existing categories with slugs
UPDATE `full-stack-ecommerce`.`product_category` SET `slug` = 'books' WHERE `category_name` = 'Books';
UPDATE `full-stack-ecommerce`.`product_category` SET `slug` = 'coffee-mugs' WHERE `category_name` = 'Coffee Mugs';
UPDATE `full-stack-ecommerce`.`product_category` SET `slug` = 'mouse-pads' WHERE `category_name` = 'Mouse Pads';
UPDATE `full-stack-ecommerce`.`product_category` SET `slug` = 'luggage-tags' WHERE `category_name` = 'Luggage Tags';

-- Set all existing categories as active
UPDATE `full-stack-ecommerce`.`product_category` SET `is_active` = 1;

-- Set display order for existing categories
UPDATE `full-stack-ecommerce`.`product_category` SET `display_order` = 1 WHERE `category_name` = 'Books';
UPDATE `full-stack-ecommerce`.`product_category` SET `display_order` = 2 WHERE `category_name` = 'Coffee Mugs';
UPDATE `full-stack-ecommerce`.`product_category` SET `display_order` = 3 WHERE `category_name` = 'Mouse Pads';
UPDATE `full-stack-ecommerce`.`product_category` SET `display_order` = 4 WHERE `category_name` = 'Luggage Tags';