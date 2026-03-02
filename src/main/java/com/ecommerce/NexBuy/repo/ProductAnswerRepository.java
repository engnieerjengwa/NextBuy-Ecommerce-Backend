package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ProductAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAnswerRepository extends JpaRepository<ProductAnswer, Long> {
}
