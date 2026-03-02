package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ProductQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {

    Page<ProductQuestion> findByProductIdOrderByDateCreatedDesc(Long productId, Pageable pageable);
}
