package com.ordersystem.capstone.fulfilment.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when an order passes fulfilment checks.
 */
public record OrderFulfilledEvent(
        UUID orderId,
        LocalDateTime processedAt
) {
}