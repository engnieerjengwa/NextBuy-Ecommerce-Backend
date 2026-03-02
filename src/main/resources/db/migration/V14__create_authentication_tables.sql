-- -----------------------------------------------------
-- V14: Create authentication tables (user, role, user_role)
-- -----------------------------------------------------

-- Role table
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- App User table (separate from customer to support all roles)
CREATE TABLE IF NOT EXISTS `app_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `mobile_number` VARCHAR(20) DEFAULT NULL,
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    `customer_id` BIGINT DEFAULT NULL,
    `date_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `last_updated` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `fk_user_customer` (`customer_id`),
    CONSTRAINT `fk_user_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- User-Role join table (many-to-many)
CREATE TABLE IF NOT EXISTS `user_role` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    KEY `fk_user_role_role` (`role_id`),
    CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Seed default roles
INSERT INTO `role` (`name`) VALUES ('ROLE_CUSTOMER');
INSERT INTO `role` (`name`) VALUES ('ROLE_SELLER');
INSERT INTO `role` (`name`) VALUES ('ROLE_ADMIN');

-- Create a default admin user (password: Admin@123 - BCrypt encoded)
-- BCrypt hash of "Admin@123"
INSERT INTO `app_user` (`first_name`, `last_name`, `email`, `password`, `enabled`)
VALUES ('System', 'Admin', 'admin@nexbuy.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IhIzG3p0CYlKAUUGJN0mBM1FMXGsaW', TRUE);

-- Assign ADMIN role to the default admin
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `app_user` u, `role` r
WHERE u.email = 'admin@nexbuy.com' AND r.name = 'ROLE_ADMIN';
