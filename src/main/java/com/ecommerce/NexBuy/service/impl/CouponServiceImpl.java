package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.CouponApplyRequestDto;
import com.ecommerce.NexBuy.dto.response.CouponResponseDto;
import com.ecommerce.NexBuy.entity.Coupon;
import com.ecommerce.NexBuy.repo.CouponRepository;
import com.ecommerce.NexBuy.service.CouponService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponResponseDto validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(code)
                .orElse(null);

        CouponResponseDto dto = new CouponResponseDto();
        if (coupon == null) {
            dto.setValid(false);
            dto.setMessage("Invalid coupon code");
            return dto;
        }

        String validationMessage = validateCouponRules(coupon);
        if (validationMessage != null) {
            dto.setValid(false);
            dto.setMessage(validationMessage);
            return dto;
        }

        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountType(coupon.getDiscountType().name());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMinimumOrderAmount(coupon.getMinimumOrderAmount());
        dto.setValid(true);
        dto.setMessage("Coupon is valid");
        return dto;
    }

    @Override
    @Transactional
    public CouponResponseDto applyCoupon(CouponApplyRequestDto requestDto) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(requestDto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid coupon code"));

        String validationMessage = validateCouponRules(coupon);
        if (validationMessage != null) {
            throw new IllegalArgumentException(validationMessage);
        }

        if (coupon.getMinimumOrderAmount() != null
                && requestDto.getOrderAmount().compareTo(coupon.getMinimumOrderAmount()) < 0) {
            throw new IllegalArgumentException(
                    "Minimum order amount of " + coupon.getMinimumOrderAmount() + " required");
        }

        BigDecimal discountAmount;
        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
            discountAmount = requestDto.getOrderAmount()
                    .multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = coupon.getDiscountValue();
        }

        // Discount cannot exceed order amount
        if (discountAmount.compareTo(requestDto.getOrderAmount()) > 0) {
            discountAmount = requestDto.getOrderAmount();
        }

        BigDecimal newTotal = requestDto.getOrderAmount().subtract(discountAmount);

        // Increment usage
        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);

        CouponResponseDto dto = new CouponResponseDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountType(coupon.getDiscountType().name());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setMinimumOrderAmount(coupon.getMinimumOrderAmount());
        dto.setValid(true);
        dto.setMessage("Coupon applied successfully");
        dto.setDiscountAmount(discountAmount);
        dto.setNewTotal(newTotal);

        logger.info("Coupon {} applied. Discount: {}, New total: {}", coupon.getCode(), discountAmount, newTotal);
        return dto;
    }

    private String validateCouponRules(Coupon coupon) {
        if (!coupon.getIsActive()) {
            return "Coupon is no longer active";
        }
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) {
            return "Coupon is not yet valid";
        }
        if (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate())) {
            return "Coupon has expired";
        }
        if (coupon.getMaxUses() != null && coupon.getCurrentUses() >= coupon.getMaxUses()) {
            return "Coupon usage limit reached";
        }
        return null;
    }
}
