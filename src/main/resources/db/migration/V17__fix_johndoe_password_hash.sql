-- -----------------------------------------------------
-- V17: Fix password hash for johndoe@test.co.za
-- Generated using Spring Security BCryptPasswordEncoder
-- Password: NextBuy@Test
-- -----------------------------------------------------

UPDATE `app_user`
SET `password` = '$2a$10$WyfMBtVvt3vELa2V2kuadOa8n0GIKZiHx.rQC0Rornx.c9hVsFUPO'
WHERE `email` = 'johndoe@test.co.za';
