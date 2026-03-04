package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.service.StripeRefundService;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeRefundServiceImpl implements StripeRefundService {

    private static final Logger logger = LoggerFactory.getLogger(StripeRefundServiceImpl.class);

    @Override
    public String processRefund(String paymentIntentId, BigDecimal amount) throws Exception {
        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            throw new IllegalArgumentException("Payment intent ID is required for refund processing");
        }

        // Convert amount to cents (Stripe uses smallest currency unit)
        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

        RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId)
                .setAmount(amountInCents)
                .build();

        logger.info("Processing Stripe refund for payment intent: {}, amount: {} cents", paymentIntentId, amountInCents);

        Refund refund = Refund.create(params);

        logger.info("Stripe refund created successfully. Refund ID: {}, Status: {}", refund.getId(), refund.getStatus());

        return refund.getId();
    }
}
