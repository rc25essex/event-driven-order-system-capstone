package com.reena.capstone.order.messaging;

import com.reena.capstone.order.event.OrderCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class KafkaOrderEventPublisherTest {

    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private KafkaOrderEventPublisher publisher;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        publisher = new KafkaOrderEventPublisher(kafkaTemplate);
    }

    @Test
    void shouldPublishOrderCreatedEvent() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                "CUSTOMER-1",
                2,
                new BigDecimal("20.00"),
                LocalDateTime.now()
        );

        publisher.publish(event);

        verify(kafkaTemplate).send(
                "order-created",
                event.orderId().toString(),
                event
        );
    }
}