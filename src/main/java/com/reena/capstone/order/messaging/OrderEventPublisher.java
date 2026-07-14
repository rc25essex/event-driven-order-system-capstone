package com.reena.capstone.order.messaging;

import com.reena.capstone.order.event.OrderCreatedEvent;

/**
 * Defines how order events are published.
 *
 * The interface decouples the Order Service from the underlying
 * messaging technology, allowing Kafka or another broker to be
 * introduced without changing the business logic.
 */
public interface OrderEventPublisher {

    /**
     * Publishes a newly created order event.
     *
     * @param event the event to publish
     */
    void publish(OrderCreatedEvent event);

}