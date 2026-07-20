package com.ordersystem.capstone.order.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;

    private String customerReference;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @ElementCollection
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {
        // Required by JPA
    }

    public Order(String customerReference, List<OrderItem> items) {

        if (customerReference == null || customerReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer reference must not be blank."
            );
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "An order must contain at least one item."
            );
        }

        this.id = UUID.randomUUID();
        this.customerReference = customerReference;
        this.items.addAll(items);
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public BigDecimal calculateTotal() {

        return items.stream()
                .map(OrderItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total quantity of all order items.
     *
     * @return the combined quantity of all items
     */
    public int calculateTotalQuantity() {

        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public void fulfil() {
        this.status = OrderStatus.FULFILLED;
    }

    public void reject() {
        this.status = OrderStatus.REJECTED;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
}