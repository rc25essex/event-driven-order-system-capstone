package com.ordersystem.capstone.order.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain event published when a new order has been successfully created.
 *
 * This immutable event is sent to Kafka so that downstream services,
 * such as the Fulfilment Service, can process the order asynchronously.
 */
public record OrderCreatedEvent(

        UUID orderId,

        String customerReference,

        int totalQuantity,

        BigDecimal totalAmount,

        LocalDateTime createdAt

) {
}