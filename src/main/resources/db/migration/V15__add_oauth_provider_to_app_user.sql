-- Add auth_provider column to app_user table for OAuth support
ALTER TABLE app_user
    ADD COLUMN auth_provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL' AFTER mobile_number;

-- Make password nullable for OAuth users (Google users have no password)
ALTER TABLE app_user
    MODIFY COLUMN password VARCHAR(255) NULL;
