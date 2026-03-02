-- Phase 4: BE-37 - Pre-order / Backorder System
ALTER TABLE product ADD COLUMN is_preorder BOOLEAN DEFAULT FALSE;
ALTER TABLE product ADD COLUMN preorder_release_date DATE;
ALTER TABLE product ADD COLUMN preorder_message VARCHAR(500);
