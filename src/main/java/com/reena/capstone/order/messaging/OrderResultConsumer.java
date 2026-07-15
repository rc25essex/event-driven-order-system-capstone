package com.reena.capstone.order.messaging;

import com.reena.capstone.fulfilment.event.OrderFulfilledEvent;
import com.reena.capstone.fulfilment.event.OrderRejectedEvent;
import com.reena.capstone.order.application.OrderService;
import com.reena.capstone.order.domain.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumes fulfilment results and updates the corresponding order.
 */
@Component
public class OrderResultConsumer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OrderResultConsumer.class);

    private final OrderService orderService;

    public OrderResultConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Handles fulfilled and rejected events received from the result topic.
     *
     * @param record Kafka record containing the fulfilment result
     */
    @KafkaListener(
            topics = "order-result",
            groupId = "order-service-group"
    )
    public void consume(ConsumerRecord<String, Object> record) {
        Object event = record.value();

        if (event instanceof OrderFulfilledEvent fulfilledEvent) {
            updateOrder(fulfilledEvent.orderId(), true);
            return;
        }

        if (event instanceof OrderRejectedEvent rejectedEvent) {
            updateOrder(rejectedEvent.orderId(), false);
            return;
        }

        throw new IllegalArgumentException(
                "Unsupported order result event: "
                        + event.getClass().getName()
        );
    }

    private void updateOrder(
            java.util.UUID orderId,
            boolean fulfilled
    ) {
        Order order = orderService.getOrder(orderId);

        if (fulfilled) {
            order.fulfil();
        } else {
            order.reject();
        }

        orderService.updateOrder(order);

        LOGGER.info(
                "Order {} updated to {}",
                orderId,
                order.getStatus()
        );
    }
}