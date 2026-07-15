package com.reena.capstone.fulfilment.messaging;

import com.reena.capstone.fulfilment.event.OrderFulfilledEvent;
import com.reena.capstone.fulfilment.event.OrderRejectedEvent;

/**
 * Publishes the outcome of fulfilment processing.
 */
public interface OrderResultPublisher {

    void publishFulfilled(OrderFulfilledEvent event);

    void publishRejected(OrderRejectedEvent event);
}