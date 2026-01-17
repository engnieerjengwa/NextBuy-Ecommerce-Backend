package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.ReturnRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    Page<ReturnRequest> findByOrderId(@Param("orderId") Long orderId, Pageable pageable);
    
    Page<ReturnRequest> findByOrderCustomerEmail(@Param("email") String email, Pageable pageable);
}