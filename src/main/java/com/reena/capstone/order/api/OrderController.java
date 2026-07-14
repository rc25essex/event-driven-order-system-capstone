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

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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

        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity
                .created(URI.create("/api/orders/" + savedOrder.getId()))
                .body(toResponse(savedOrder));
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return toResponse(orderService.getOrder(orderId));
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemRequest> items = order.getItems()
                .stream()
                .map(item -> new OrderItemRequest(
                        item.getProductReference(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

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