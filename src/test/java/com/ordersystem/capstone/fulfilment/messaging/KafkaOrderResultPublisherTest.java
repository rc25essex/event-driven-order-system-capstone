package com.ordersystem.capstone.fulfilment.messaging;

import com.ordersystem.capstone.fulfilment.event.OrderFulfilledEvent;
import com.ordersystem.capstone.fulfilment.event.OrderRejectedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KafkaOrderResultPublisherTest {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private KafkaOrderResultPublisher publisher;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        publisher = new KafkaOrderResultPublisher(kafkaTemplate);
    }

    @Test
    void shouldPublishFulfilledEvent() {
        OrderFulfilledEvent event = new OrderFulfilledEvent(
                UUID.randomUUID(),
                LocalDateTime.now()
        );

        publisher.publishFulfilled(event);

        verify(kafkaTemplate).send(
                "order-result",
                event.orderId().toString(),
                event
        );
    }

    @Test
    void shouldPublishRejectedEvent() {
        OrderRejectedEvent event = new OrderRejectedEvent(
                UUID.randomUUID(),
                "Order quantity exceeds limit.",
                LocalDateTime.now()
        );

        publisher.publishRejected(event);

        verify(kafkaTemplate).send(
                "order-result",
                event.orderId().toString(),
                event
        );
    }
}