package com.ordersystem.capstone.fulfilment.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when an order cannot be fulfilled.
 */
public record OrderRejectedEvent(
        UUID orderId,
        String reason,
        LocalDateTime processedAt
) {
}