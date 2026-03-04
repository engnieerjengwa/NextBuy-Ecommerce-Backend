package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.LoyaltyResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.LoyaltyProgram;
import com.ecommerce.NexBuy.entity.LoyaltyTransaction;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.LoyaltyProgramRepository;
import com.ecommerce.NexBuy.service.LoyaltyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {

    private static final Logger logger = LoggerFactory.getLogger(LoyaltyServiceImpl.class);

    private static final int POINTS_PER_DOLLAR = 1;
    private static final int MIN_REDEEM_POINTS = 100;
    private static final double POINTS_TO_DOLLAR_RATE = 0.01; // 100 points = $1

    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final CustomerRepository customerRepository;

    public LoyaltyServiceImpl(LoyaltyProgramRepository loyaltyProgramRepository,
                              CustomerRepository customerRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyResponseDto getLoyaltyStatus(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        LoyaltyProgram program = loyaltyProgramRepository.findByCustomerId(customer.getId())
                .orElse(null);

        if (program == null) {
            return buildEmptyLoyaltyResponse();
        }

        return mapToResponseDto(program);
    }

    @Override
    @Transactional
    public LoyaltyResponseDto earnPoints(String customerEmail, int points, String source, Long orderId) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        LoyaltyProgram program = loyaltyProgramRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    LoyaltyProgram newProgram = new LoyaltyProgram();
                    newProgram.setCustomer(customer);
                    newProgram.setTier(LoyaltyProgram.LoyaltyTier.BRONZE);
                    newProgram.setTotalPoints(0);
                    newProgram.setLifetimePoints(0);
                    return loyaltyProgramRepository.save(newProgram);
                });

        // Apply tier multiplier
        int multipliedPoints = applyTierMultiplier(points, program.getTier());

        // Add points
        program.setTotalPoints(program.getTotalPoints() + multipliedPoints);
        program.setLifetimePoints(program.getLifetimePoints() + multipliedPoints);

        // Update tier based on lifetime points
        LoyaltyProgram.LoyaltyTier newTier = LoyaltyProgram.LoyaltyTier.fromPoints(program.getLifetimePoints());
        if (newTier != program.getTier()) {
            logger.info("Customer {} tier upgraded from {} to {}", customerEmail, program.getTier(), newTier);
            program.setTier(newTier);
            program.setTierExpiryDate(LocalDate.now().plusYears(1));
        }

        // Create transaction
        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setLoyaltyProgram(program);
        transaction.setPoints(multipliedPoints);
        transaction.setType(LoyaltyTransaction.LoyaltyTransactionType.EARNED);
        transaction.setSource(source);
        transaction.setOrderId(orderId);
        program.getTransactions().add(transaction);

        loyaltyProgramRepository.save(program);
        logger.info("Customer {} earned {} points (source: {}, orderId: {})", customerEmail, multipliedPoints, source, orderId);

        return mapToResponseDto(program);
    }

    @Override
    @Transactional
    public LoyaltyResponseDto redeemPoints(String customerEmail, int points) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        LoyaltyProgram program = loyaltyProgramRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new IllegalArgumentException("Loyalty program not found. You need to earn points first."));

        if (points < MIN_REDEEM_POINTS) {
            throw new IllegalArgumentException("Minimum redemption is " + MIN_REDEEM_POINTS + " points");
        }

        if (program.getTotalPoints() < points) {
            throw new IllegalArgumentException("Insufficient points. You have " + program.getTotalPoints() + " points.");
        }

        // Deduct points
        program.setTotalPoints(program.getTotalPoints() - points);

        // Create transaction
        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setLoyaltyProgram(program);
        transaction.setPoints(-points);
        transaction.setType(LoyaltyTransaction.LoyaltyTransactionType.REDEEMED);
        transaction.setSource("REDEMPTION");
        program.getTransactions().add(transaction);

        loyaltyProgramRepository.save(program);
        logger.info("Customer {} redeemed {} points (${} value)", customerEmail, points, points * POINTS_TO_DOLLAR_RATE);

        return mapToResponseDto(program);
    }

    private int applyTierMultiplier(int basePoints, LoyaltyProgram.LoyaltyTier tier) {
        return switch (tier) {
            case BRONZE -> basePoints;
            case SILVER -> (int) (basePoints * 1.25);
            case GOLD -> (int) (basePoints * 1.5);
            case PLATINUM -> (int) (basePoints * 2.0);
        };
    }

    private LoyaltyResponseDto buildEmptyLoyaltyResponse() {
        LoyaltyResponseDto dto = new LoyaltyResponseDto();
        dto.setTier("BRONZE");
        dto.setTotalPoints(0);
        dto.setLifetimePoints(0);
        dto.setNextTier("SILVER");
        dto.setPointsToNextTier(LoyaltyProgram.LoyaltyTier.SILVER.getRequiredPoints());
        dto.setProgressPercentage(0);
        dto.setRecentTransactions(List.of());
        dto.setTierBenefits(buildTierBenefits());
        return dto;
    }

    private LoyaltyResponseDto mapToResponseDto(LoyaltyProgram program) {
        LoyaltyResponseDto dto = new LoyaltyResponseDto();
        dto.setTier(program.getTier().name());
        dto.setTotalPoints(program.getTotalPoints());
        dto.setLifetimePoints(program.getLifetimePoints());
        dto.setTierExpiryDate(program.getTierExpiryDate());
        dto.setDateJoined(program.getDateJoined());

        LoyaltyProgram.LoyaltyTier nextTier = program.getTier().nextTier();
        dto.setNextTier(nextTier.name());

        if (program.getTier() == LoyaltyProgram.LoyaltyTier.PLATINUM) {
            dto.setPointsToNextTier(0);
            dto.setProgressPercentage(100);
        } else {
            int pointsNeeded = nextTier.getRequiredPoints() - program.getLifetimePoints();
            dto.setPointsToNextTier(Math.max(0, pointsNeeded));

            int currentTierPoints = program.getTier().getRequiredPoints();
            int nextTierPoints = nextTier.getRequiredPoints();
            int range = nextTierPoints - currentTierPoints;
            int progress = program.getLifetimePoints() - currentTierPoints;
            dto.setProgressPercentage(range > 0 ? Math.min(100, (progress * 100.0) / range) : 0);
        }

        List<LoyaltyResponseDto.LoyaltyTransactionDto> transactions = program.getTransactions().stream()
                .sorted((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()))
                .limit(20)
                .map(this::mapTransactionToDto)
                .collect(Collectors.toList());
        dto.setRecentTransactions(transactions);

        dto.setTierBenefits(buildTierBenefits());
        return dto;
    }

    private LoyaltyResponseDto.LoyaltyTransactionDto mapTransactionToDto(LoyaltyTransaction tx) {
        LoyaltyResponseDto.LoyaltyTransactionDto dto = new LoyaltyResponseDto.LoyaltyTransactionDto();
        dto.setId(tx.getId());
        dto.setPoints(tx.getPoints());
        dto.setType(tx.getType().name());
        dto.setSource(tx.getSource());
        dto.setOrderId(tx.getOrderId());
        dto.setDateCreated(tx.getDateCreated());
        return dto;
    }

    private List<LoyaltyResponseDto.TierBenefit> buildTierBenefits() {
        return List.of(
                createBenefit("BRONZE", 0, "Earn 1 point per $1 spent"),
                createBenefit("SILVER", 500, "1.25x points multiplier + free standard delivery"),
                createBenefit("GOLD", 2000, "1.5x points multiplier + priority support + exclusive deals"),
                createBenefit("PLATINUM", 5000, "2x points multiplier + free express delivery + early access to sales")
        );
    }

    private LoyaltyResponseDto.TierBenefit createBenefit(String tier, int requiredPoints, String description) {
        LoyaltyResponseDto.TierBenefit benefit = new LoyaltyResponseDto.TierBenefit();
        benefit.setTier(tier);
        benefit.setRequiredPoints(requiredPoints);
        benefit.setDescription(description);
        return benefit;
    }
}
