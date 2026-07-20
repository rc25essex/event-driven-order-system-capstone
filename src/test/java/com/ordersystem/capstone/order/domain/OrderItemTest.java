package com.ordersystem.capstone.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldCalculateSubtotal() {
        OrderItem item = new OrderItem(
                "PROD-101",
                2,
                new BigDecimal("24.99")
        );

        assertEquals(
                new BigDecimal("49.98"),
                item.calculateSubtotal()
        );
    }

    @Test
    void shouldRejectBlankProductReference() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem(
                        " ",
                        1,
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldRejectZeroQuantity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem(
                        "PROD-101",
                        0,
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldRejectZeroUnitPrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OrderItem(
                        "PROD-101",
                        1,
                        BigDecimal.ZERO
                )
        );
    }
}