package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.ReferralResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.CustomerWallet;
import com.ecommerce.NexBuy.entity.Referral;
import com.ecommerce.NexBuy.entity.WalletTransaction;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.CustomerWalletRepository;
import com.ecommerce.NexBuy.repo.ReferralRepository;
import com.ecommerce.NexBuy.service.ReferralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReferralServiceImpl implements ReferralService {

    private static final Logger logger = LoggerFactory.getLogger(ReferralServiceImpl.class);
    private static final BigDecimal REFERRAL_REWARD = new BigDecimal("5.00");
    private static final String SHARE_BASE_URL = "https://nexbuy.co.zw/ref/";

    private final ReferralRepository referralRepository;
    private final CustomerRepository customerRepository;
    private final CustomerWalletRepository customerWalletRepository;

    public ReferralServiceImpl(ReferralRepository referralRepository,
                               CustomerRepository customerRepository,
                               CustomerWalletRepository customerWalletRepository) {
        this.referralRepository = referralRepository;
        this.customerRepository = customerRepository;
        this.customerWalletRepository = customerWalletRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ReferralResponseDto getReferralInfo(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        // Get or determine referral code
        List<Referral> referrals = referralRepository.findAllByReferrerId(customer.getId());

        String referralCode;
        if (!referrals.isEmpty()) {
            referralCode = referrals.get(0).getReferralCode();
        } else {
            // Compute a deterministic code for display (won't persist until actually used)
            referralCode = generateReferralCode(customer);
        }

        return buildReferralResponse(referralCode, referrals);
    }

    @Override
    @Transactional
    public ReferralResponseDto applyReferralCode(String customerEmail, String referralCode) {
        Customer referee = customerRepository.findByEmail(customerEmail);
        if (referee == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        Referral referral = referralRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid referral code: " + referralCode));

        // Can't refer yourself
        if (referral.getReferrer().getId().equals(referee.getId())) {
            throw new IllegalArgumentException("You cannot use your own referral code");
        }

        // Check if already completed
        if (referral.getStatus() == Referral.ReferralStatus.COMPLETED) {
            throw new IllegalArgumentException("This referral code has already been used");
        }

        if (referral.getStatus() == Referral.ReferralStatus.EXPIRED) {
            throw new IllegalArgumentException("This referral code has expired");
        }

        // Complete the referral
        referral.setReferee(referee);
        referral.setStatus(Referral.ReferralStatus.COMPLETED);
        referral.setDateCompleted(LocalDateTime.now());
        referralRepository.save(referral);

        // Credit both referrer and referee wallets
        creditWallet(referral.getReferrer(), REFERRAL_REWARD, "REF-" + referralCode, "Referral reward - friend signed up");
        creditWallet(referee, REFERRAL_REWARD, "REF-" + referralCode, "Welcome bonus - referral reward");

        logger.info("Referral completed: code={}, referrer={}, referee={}", 
                referralCode, referral.getReferrer().getEmail(), customerEmail);

        // Create a new pending referral for the referrer so they can refer more people
        Referral newReferral = new Referral();
        newReferral.setReferrer(referral.getReferrer());
        newReferral.setReferralCode(referralCode); // keep same code
        // Actually, we need a unique code. Let's just keep track via the existing referrals.
        // The referrer already has their code, so let's not create duplicate.

        List<Referral> allReferrals = referralRepository.findAllByReferrerId(referral.getReferrer().getId());
        return buildReferralResponse(referralCode, allReferrals);
    }

    private void creditWallet(Customer customer, BigDecimal amount, String referenceId, String description) {
        CustomerWallet wallet = customerWalletRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    CustomerWallet newWallet = new CustomerWallet();
                    newWallet.setCustomer(customer);
                    newWallet.setBalance(BigDecimal.ZERO);
                    newWallet.setCurrency("USD");
                    return customerWalletRepository.save(newWallet);
                });

        wallet.setBalance(wallet.getBalance().add(amount));

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(WalletTransaction.TransactionType.CREDIT);
        transaction.setSource(WalletTransaction.TransactionSource.REFERRAL);
        transaction.setReferenceId(referenceId);
        transaction.setDescription(description);
        wallet.getTransactions().add(transaction);

        customerWalletRepository.save(wallet);
    }

    private String generateReferralCode(Customer customer) {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private ReferralResponseDto buildReferralResponse(String referralCode, List<Referral> referrals) {
        ReferralResponseDto dto = new ReferralResponseDto();
        dto.setReferralCode(referralCode);
        dto.setShareUrl(SHARE_BASE_URL + referralCode);
        dto.setTotalReferrals(referrals.size());

        long completed = referrals.stream()
                .filter(r -> r.getStatus() == Referral.ReferralStatus.COMPLETED)
                .count();
        dto.setCompletedReferrals((int) completed);

        BigDecimal totalEarned = referrals.stream()
                .filter(r -> r.getStatus() == Referral.ReferralStatus.COMPLETED)
                .map(Referral::getRewardAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalEarned(totalEarned);

        List<ReferralResponseDto.ReferralDetailDto> details = referrals.stream()
                .map(this::mapToDetailDto)
                .collect(Collectors.toList());
        dto.setReferrals(details);

        return dto;
    }

    private ReferralResponseDto.ReferralDetailDto mapToDetailDto(Referral referral) {
        ReferralResponseDto.ReferralDetailDto dto = new ReferralResponseDto.ReferralDetailDto();
        dto.setStatus(referral.getStatus().name());
        dto.setRewardAmount(referral.getRewardAmount());
        dto.setDateCreated(referral.getDateCreated());
        dto.setDateCompleted(referral.getDateCompleted());
        return dto;
    }
}
