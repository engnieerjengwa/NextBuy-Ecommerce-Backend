-- -----------------------------------------------------
-- V16: Add admin user johndoe@test.co.za
-- Password: NextBuy@Test (BCrypt encoded)
-- Roles: ROLE_ADMIN, ROLE_CUSTOMER
-- -----------------------------------------------------

INSERT INTO `app_user` (`first_name`, `last_name`, `email`, `password`, `enabled`)
VALUES ('John', 'Doe', 'johndoe@test.co.za',
        '$2a$10$WyfMBtVvt3vELa2V2kuadOa8n0GIKZiHx.rQC0Rornx.c9hVsFUPO', TRUE);

-- Assign ROLE_ADMIN
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `app_user` u, `role` r
WHERE u.email = 'johndoe@test.co.za' AND r.name = 'ROLE_ADMIN';

-- Assign ROLE_CUSTOMER
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `app_user` u, `role` r
WHERE u.email = 'johndoe@test.co.za' AND r.name = 'ROLE_CUSTOMER';
