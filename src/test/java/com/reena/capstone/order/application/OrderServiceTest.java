package com.reena.capstone.order.application;

import com.reena.capstone.order.domain.Order;
import com.reena.capstone.order.domain.OrderItem;
import com.reena.capstone.order.event.OrderCreatedEvent;
import com.reena.capstone.order.messaging.OrderEventPublisher;
import com.reena.capstone.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OrderService}.
 *
 * Repository and messaging dependencies are mocked so the service logic
 * can be tested without starting Spring, H2 or Kafka.
 */
class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderEventPublisher orderEventPublisher;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderEventPublisher = mock(OrderEventPublisher.class);

        orderService = new OrderService(
                orderRepository,
                orderEventPublisher
        );
    }

    @Test
    void shouldSaveOrderAndPublishOrderCreatedEvent() {
        Order order = createOrder();

        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertSame(order, savedOrder);
        verify(orderRepository).save(order);

        ArgumentCaptor<OrderCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(OrderCreatedEvent.class);

        verify(orderEventPublisher).publish(eventCaptor.capture());

        OrderCreatedEvent publishedEvent = eventCaptor.getValue();

        assertEquals(order.getId(), publishedEvent.orderId());
        assertEquals(
                order.getCustomerReference(),
                publishedEvent.customerReference()
        );
        assertEquals(
                order.calculateTotal(),
                publishedEvent.totalAmount()
        );
        assertEquals(
                order.getCreatedAt(),
                publishedEvent.createdAt()
        );
    }

    @Test
    void shouldReturnOrderWhenOrderExists() {
        Order order = createOrder();
        UUID orderId = order.getId();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        Order result = orderService.getOrder(orderId);

        assertSame(order, result);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.getOrder(orderId)
        );

        assertEquals("Order not found.", exception.getMessage());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldReturnAllOrders() {
        List<Order> orders = List.of(
                createOrder(),
                createOrder()
        );

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(orders, result);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldUpdateOrder() {
        Order order = createOrder();
        order.fulfil();

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrder(order);

        assertSame(order, result);
        assertEquals(
                com.reena.capstone.order.domain.OrderStatus.FULFILLED,
                result.getStatus()
        );
        verify(orderRepository).save(order);
    }

    private Order createOrder() {
        return new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem(
                                "PEN",
                                2,
                                new BigDecimal("10.00")
                        )
                )
        );
    }
}