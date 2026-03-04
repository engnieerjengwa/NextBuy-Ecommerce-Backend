package com.ecommerce.NexBuy.service;

import java.math.BigDecimal;

public interface StripeRefundService {

    /**
     * Process a full refund for a return request via Stripe.
     *
     * @param paymentIntentId the Stripe payment intent ID
     * @param amount the refund amount
     * @return the Stripe refund ID
     */
    String processRefund(String paymentIntentId, BigDecimal amount) throws Exception;
}
