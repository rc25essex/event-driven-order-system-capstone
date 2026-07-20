package com.ordersystem.capstone.order.api.dto;

import com.ordersystem.capstone.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        String customerReference,
        OrderStatus status,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItemRequest> items
) {
}