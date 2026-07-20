package com.ordersystem.capstone.order.application;

import com.ordersystem.capstone.order.domain.Order;
import com.ordersystem.capstone.order.event.OrderCreatedEvent;
import com.ordersystem.capstone.order.messaging.OrderEventPublisher;
import com.ordersystem.capstone.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Coordinates order persistence and event publication.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderEventPublisher orderEventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    /**
     * Persists a new order and publishes an event for asynchronous fulfilment.
     */
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerReference(),
                savedOrder.calculateTotalQuantity(),
                savedOrder.calculateTotal(),
                savedOrder.getCreatedAt()
        );

        orderEventPublisher.publish(event);

        return savedOrder;
    }

    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found.")
                );
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}