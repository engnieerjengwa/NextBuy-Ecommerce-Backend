package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.FrequentlyBoughtTogether;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrequentlyBoughtTogetherRepository extends JpaRepository<FrequentlyBoughtTogether, Long> {
    List<FrequentlyBoughtTogether> findByProductIdOrderByCoPurchaseCountDesc(Long productId);
}
