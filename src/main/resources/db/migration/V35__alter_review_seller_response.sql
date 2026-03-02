-- Phase 4: BE-45 - Review Response System
ALTER TABLE review ADD COLUMN seller_response TEXT;
ALTER TABLE review ADD COLUMN seller_response_date DATETIME;
