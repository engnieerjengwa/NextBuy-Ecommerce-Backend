package com.ecommerce.NexBuy.entity;

import java.util.*;

/**
 * Enum representing valid order statuses and their allowed transitions.
 */
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static {
        VALID_TRANSITIONS.put(PLACED, EnumSet.of(CONFIRMED, CANCELLED));
        VALID_TRANSITIONS.put(CONFIRMED, EnumSet.of(PROCESSING, CANCELLED));
        VALID_TRANSITIONS.put(PROCESSING, EnumSet.of(SHIPPED, CANCELLED));
        VALID_TRANSITIONS.put(SHIPPED, EnumSet.of(OUT_FOR_DELIVERY));
        VALID_TRANSITIONS.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED));
        VALID_TRANSITIONS.put(DELIVERED, EnumSet.noneOf(OrderStatus.class));
        VALID_TRANSITIONS.put(CANCELLED, EnumSet.noneOf(OrderStatus.class));
    }

    /**
     * Returns whether transitioning from this status to the given target status is allowed.
     */
    public boolean canTransitionTo(OrderStatus target) {
        Set<OrderStatus> allowed = VALID_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }

    /**
     * Parses a string into an OrderStatus, case-insensitive.
     * Throws IllegalArgumentException if the value is not a valid status.
     */
    public static OrderStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Order status cannot be null or blank");
        }
        try {
            return OrderStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid order status: '" + value + "'. Valid statuses are: " + Arrays.toString(OrderStatus.values()));
        }
    }
}
