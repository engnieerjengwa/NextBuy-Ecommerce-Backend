package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    Optional<Referral> findByReferralCode(String referralCode);
    Optional<Referral> findByReferrerId(Long referrerId);
    List<Referral> findAllByReferrerId(Long referrerId);
    boolean existsByReferrerId(Long referrerId);
}
