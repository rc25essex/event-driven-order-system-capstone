package com.reena.capstone.order.api;

import com.reena.capstone.order.api.dto.CreateOrderRequest;
import com.reena.capstone.order.api.dto.OrderItemRequest;
import com.reena.capstone.order.api.dto.OrderResponse;
import com.reena.capstone.order.application.OrderService;
import com.reena.capstone.order.domain.Order;
import com.reena.capstone.order.domain.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller responsible for handling order-related API requests.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor injection of the OrderService.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new customer order.
     * Validates the request, converts DTOs into domain objects,
     * delegates processing to the service layer and returns
     * HTTP 201 Created with the order details.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        List<OrderItem> items = request.items()
                .stream()
                .map(item -> new OrderItem(
                        item.productReference(),
                        item.quantity(),
                        item.unitPrice()
                ))
                .toList();

        Order order = new Order(
                request.customerReference(),
                items
        );

        // Delegate order creation to the service layer
        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity
                .created(URI.create("/api/orders/" + savedOrder.getId()))
                .body(toResponse(savedOrder));
    }

    /**
     * Retrieves a single order by its unique identifier.
     */
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return toResponse(orderService.getOrder(orderId));
    }

    /**
     * Retrieves all customer orders.
     */
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converts a domain Order object into an API response DTO.
     */
    private OrderResponse toResponse(Order order) {
        List<OrderItemRequest> items = order.getItems()
                .stream()
                .map(item -> new OrderItemRequest(
                        item.getProductReference(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        // Build and return the response object
        return new OrderResponse(
                order.getId(),
                order.getCustomerReference(),
                order.getStatus(),
                order.calculateTotal(),
                order.getCreatedAt(),
                items
        );
    }
}