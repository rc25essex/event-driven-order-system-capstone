package com.reena.capstone.order.messaging;

import com.reena.capstone.fulfilment.event.OrderFulfilledEvent;
import com.reena.capstone.fulfilment.event.OrderRejectedEvent;
import com.reena.capstone.order.application.OrderService;
import com.reena.capstone.order.domain.Order;
import com.reena.capstone.order.domain.OrderItem;
import com.reena.capstone.order.domain.OrderStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderResultConsumerTest {

    private OrderService orderService;
    private OrderResultConsumer consumer;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        consumer = new OrderResultConsumer(orderService);
    }

    @Test
    void shouldMarkOrderAsFulfilled() {

        Order order = createOrder();

        when(orderService.getOrder(order.getId()))
                .thenReturn(order);

        ConsumerRecord<String, Object> record =
                new ConsumerRecord<>(
                        "order-result",
                        0,
                        0,
                        order.getId().toString(),
                        new OrderFulfilledEvent(
                                order.getId(),
                                LocalDateTime.now()
                        )
                );

        consumer.consume(record);

        assertEquals(OrderStatus.FULFILLED, order.getStatus());

        verify(orderService).updateOrder(order);
    }

    @Test
    void shouldMarkOrderAsRejected() {

        Order order = createOrder();

        when(orderService.getOrder(order.getId()))
                .thenReturn(order);

        OrderRejectedEvent event = new OrderRejectedEvent(
                order.getId(),
                "Quantity exceeded",
                LocalDateTime.now()
        );

        ConsumerRecord<String, Object> record =
                new ConsumerRecord<>(
                        "order-result",
                        0,
                        0L,
                        order.getId().toString(),
                        event
                );

        consumer.consume(record);

        assertEquals(OrderStatus.REJECTED, order.getStatus());

        verify(orderService).getOrder(order.getId());
        verify(orderService).updateOrder(order);
    }

    private Order createOrder() {

        return new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem(
                                "PEN",
                                2,
                                new BigDecimal("10.00")
                        )
                )
        );
    }
}