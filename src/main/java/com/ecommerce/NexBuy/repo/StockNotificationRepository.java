package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.StockNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockNotificationRepository extends JpaRepository<StockNotification, Long> {

    Optional<StockNotification> findByProductIdAndCustomerEmail(Long productId, String customerEmail);

    boolean existsByProductIdAndCustomerEmail(Long productId, String customerEmail);

    List<StockNotification> findByProductIdAndIsNotifiedFalse(Long productId);
}
