package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    Page<LoyaltyTransaction> findByLoyaltyProgramIdOrderByDateCreatedDesc(Long loyaltyProgramId, Pageable pageable);
}
