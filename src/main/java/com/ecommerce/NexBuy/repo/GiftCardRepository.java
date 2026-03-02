package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByCodeIgnoreCase(String code);
    List<GiftCard> findByPurchaserId(Long purchaserId);
}
