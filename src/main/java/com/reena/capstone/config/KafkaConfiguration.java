package com.reena.capstone.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka configuration for the Event-Driven Order Processing System.
 *
 * Defines the application topics used for asynchronous communication.
 */
@Configuration
public class KafkaConfiguration {

    public static final String ORDER_CREATED_TOPIC = "order-created";
    public static final String ORDER_RESULT_TOPIC = "order-result";

    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic(ORDER_CREATED_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic orderResultTopic() {
        return new NewTopic(ORDER_RESULT_TOPIC, 1, (short) 1);
    }
}