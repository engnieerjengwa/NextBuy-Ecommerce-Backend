package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query("SELECT d FROM Deal d WHERE d.isActive = true AND d.startTime <= :now AND d.endTime >= :now " +
           "AND (d.maxQuantity IS NULL OR d.soldQuantity < d.maxQuantity)")
    List<Deal> findActiveDeals(@Param("now") LocalDateTime now);

    @Query("SELECT d FROM Deal d WHERE d.isActive = true AND d.dealType = 'DAILY_DEAL' " +
           "AND d.startTime <= :now AND d.endTime >= :now " +
           "AND (d.maxQuantity IS NULL OR d.soldQuantity < d.maxQuantity)")
    List<Deal> findActiveDailyDeals(@Param("now") LocalDateTime now);

    @Query("SELECT d FROM Deal d WHERE d.isActive = true AND d.dealType = 'FLASH_SALE' " +
           "AND d.startTime <= :now AND d.endTime >= :now " +
           "AND (d.maxQuantity IS NULL OR d.soldQuantity < d.maxQuantity)")
    List<Deal> findActiveFlashSales(@Param("now") LocalDateTime now);

    List<Deal> findByProductId(Long productId);
}
