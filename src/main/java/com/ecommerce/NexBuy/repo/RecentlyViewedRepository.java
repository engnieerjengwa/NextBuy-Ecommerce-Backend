package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.RecentlyViewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long> {

    List<RecentlyViewed> findTop20ByCustomerIdOrderByViewedAtDesc(Long customerId);

    Optional<RecentlyViewed> findByCustomerIdAndProductId(Long customerId, Long productId);

    @Modifying
    @Query("DELETE FROM RecentlyViewed rv WHERE rv.customer.id = :customerId AND rv.id NOT IN " +
           "(SELECT rv2.id FROM RecentlyViewed rv2 WHERE rv2.customer.id = :customerId ORDER BY rv2.viewedAt DESC LIMIT 20)")
    void keepOnlyLatest20(@Param("customerId") Long customerId);
}
