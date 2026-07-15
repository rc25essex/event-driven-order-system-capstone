package com.reena.capstone.fulfilment.messaging;

import com.reena.capstone.fulfilment.application.FulfilmentService;
import com.reena.capstone.fulfilment.event.OrderFulfilledEvent;
import com.reena.capstone.fulfilment.event.OrderRejectedEvent;
import com.reena.capstone.order.event.OrderCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderCreatedConsumerTest {

    private FulfilmentService fulfilmentService;
    private OrderResultPublisher orderResultPublisher;
    private OrderCreatedConsumer consumer;

    @BeforeEach
    void setUp() {
        fulfilmentService = mock(FulfilmentService.class);
        orderResultPublisher = mock(OrderResultPublisher.class);

        consumer = new OrderCreatedConsumer(
                fulfilmentService,
                orderResultPublisher
        );
    }

    @Test
    void shouldPublishFulfilledEventWhenOrderCanBeFulfilled() {
        OrderCreatedEvent event = createEvent(10);

        when(fulfilmentService.canFulfil(10)).thenReturn(true);

        consumer.consume(event);

        verify(orderResultPublisher)
                .publishFulfilled(any(OrderFulfilledEvent.class));

        verify(orderResultPublisher, never())
                .publishRejected(any(OrderRejectedEvent.class));
    }

    @Test
    void shouldPublishRejectedEventWhenOrderCannotBeFulfilled() {
        OrderCreatedEvent event = createEvent(25);

        when(fulfilmentService.canFulfil(25)).thenReturn(false);

        consumer.consume(event);

        verify(orderResultPublisher)
                .publishRejected(any(OrderRejectedEvent.class));

        verify(orderResultPublisher, never())
                .publishFulfilled(any(OrderFulfilledEvent.class));
    }

    private OrderCreatedEvent createEvent(int quantity) {
        return new OrderCreatedEvent(
                UUID.randomUUID(),
                "CUSTOMER-1",
                quantity,
                new BigDecimal("20.00"),
                LocalDateTime.now()
        );
    }
}