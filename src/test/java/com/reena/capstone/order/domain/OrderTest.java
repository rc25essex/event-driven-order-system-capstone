package com.reena.capstone.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCalculateOrderTotal() {

        OrderItem first = new OrderItem(
                "PEN",
                2,
                new BigDecimal("10.00")
        );

        OrderItem second = new OrderItem(
                "BOOK",
                1,
                new BigDecimal("5.00")
        );

        Order order = new Order(
                "CUSTOMER-1",
                List.of(first, second)
        );

        assertEquals(
                new BigDecimal("25.00"),
                order.calculateTotal()
        );
    }

    @Test
    void shouldCreatePendingOrder() {

        Order order = new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem(
                                "PEN",
                                1,
                                new BigDecimal("5.00")
                        )
                )
        );

        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void shouldFulfilOrder() {

        Order order = new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem(
                                "PEN",
                                1,
                                new BigDecimal("5.00")
                        )
                )
        );

        order.fulfil();

        assertEquals(OrderStatus.FULFILLED, order.getStatus());
    }

    @Test
    void shouldCalculateTotalQuantity() {
        Order order = new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem("PEN", 2, new BigDecimal("10.00")),
                        new OrderItem("BOOK", 3, new BigDecimal("5.00"))
                )
        );

        assertEquals(5, order.calculateTotalQuantity());
    }
}