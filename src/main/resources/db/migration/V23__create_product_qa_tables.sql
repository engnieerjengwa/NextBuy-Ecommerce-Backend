-- Phase 3: BE-42 — Product Q&A system
CREATE TABLE product_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    is_answered BOOLEAN DEFAULT FALSE,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE product_answer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    answered_by_customer_id BIGINT,
    answered_by_seller BOOLEAN DEFAULT FALSE,
    answer TEXT NOT NULL,
    helpful_count INT DEFAULT 0,
    date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES product_question(id) ON DELETE CASCADE
);

CREATE INDEX idx_product_question_product_id ON product_question(product_id);
CREATE INDEX idx_product_answer_question_id ON product_answer(question_id);
