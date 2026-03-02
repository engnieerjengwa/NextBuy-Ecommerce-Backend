package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.SavedAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedAddressRepository extends JpaRepository<SavedAddress, Long> {

    List<SavedAddress> findByCustomerIdOrderByIsDefaultDescDateCreatedDesc(Long customerId);

    Optional<SavedAddress> findByIdAndCustomerId(Long id, Long customerId);

    @Modifying
    @Query("UPDATE SavedAddress s SET s.isDefault = false WHERE s.customer.id = :customerId AND s.id <> :addressId")
    void clearDefaultForCustomer(@Param("customerId") Long customerId, @Param("addressId") Long addressId);
}
