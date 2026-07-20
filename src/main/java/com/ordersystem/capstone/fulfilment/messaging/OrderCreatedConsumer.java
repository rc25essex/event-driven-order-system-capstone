package com.ordersystem.capstone.fulfilment.messaging;

import com.ordersystem.capstone.fulfilment.application.FulfilmentService;
import com.ordersystem.capstone.fulfilment.event.OrderFulfilledEvent;
import com.ordersystem.capstone.fulfilment.event.OrderRejectedEvent;
import com.ordersystem.capstone.order.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Consumes newly created orders and publishes their fulfilment outcome.
 */
@Component
public class OrderCreatedConsumer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private static final String REJECTION_REASON =
            "Total order quantity exceeds the fulfilment limit.";

    private final FulfilmentService fulfilmentService;
    private final OrderResultPublisher orderResultPublisher;

    public OrderCreatedConsumer(
            FulfilmentService fulfilmentService,
            OrderResultPublisher orderResultPublisher
    ) {
        this.fulfilmentService = fulfilmentService;
        this.orderResultPublisher = orderResultPublisher;
    }

    /**
     * Processes an order asynchronously after receiving its creation event.
     *
     * @param event newly created order event
     */
    @KafkaListener(
            topics = "order-created",
            groupId = "fulfilment-service-group"
    )
    public void consume(OrderCreatedEvent event) {
        LOGGER.info(
                "Processing OrderCreatedEvent for order {}",
                event.orderId()
        );

        if (fulfilmentService.canFulfil(event.totalQuantity())) {
            orderResultPublisher.publishFulfilled(
                    new OrderFulfilledEvent(
                            event.orderId(),
                            LocalDateTime.now()
                    )
            );

            LOGGER.info("Order {} was fulfilled", event.orderId());
            return;
        }

        orderResultPublisher.publishRejected(
                new OrderRejectedEvent(
                        event.orderId(),
                        REJECTION_REASON,
                        LocalDateTime.now()
                )
        );

        LOGGER.info("Order {} was rejected", event.orderId());
    }
}