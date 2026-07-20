package com.ordersystem.capstone.fulfilment.messaging;

import com.ordersystem.capstone.fulfilment.event.OrderFulfilledEvent;
import com.ordersystem.capstone.fulfilment.event.OrderRejectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes fulfilment outcomes to the Kafka order-result topic.
 */
@Component
public class KafkaOrderResultPublisher implements OrderResultPublisher {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(KafkaOrderResultPublisher.class);

    private static final String ORDER_RESULT_TOPIC = "order-result";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderResultPublisher(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishFulfilled(OrderFulfilledEvent event) {
        LOGGER.info(
                "Publishing OrderFulfilledEvent for order {}",
                event.orderId()
        );

        kafkaTemplate.send(
                ORDER_RESULT_TOPIC,
                event.orderId().toString(),
                event
        );
    }

    @Override
    public void publishRejected(OrderRejectedEvent event) {
        LOGGER.info(
                "Publishing OrderRejectedEvent for order {}",
                event.orderId()
        );

        kafkaTemplate.send(
                ORDER_RESULT_TOPIC,
                event.orderId().toString(),
                event
        );
    }
}