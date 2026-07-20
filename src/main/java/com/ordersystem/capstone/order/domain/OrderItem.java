package com.ordersystem.capstone.order.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class OrderItem {

    private String productReference;
    private int quantity;
    private BigDecimal unitPrice;

    protected OrderItem() {
        // Required by JPA
    }

    public OrderItem(
            String productReference,
            int quantity,
            BigDecimal unitPrice
    ) {
        if (productReference == null || productReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Product reference must not be blank."
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Unit price must be greater than zero."
            );
        }

        this.productReference = productReference;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getProductReference() {
        return productReference;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof OrderItem orderItem)) {
            return false;
        }

        return quantity == orderItem.quantity
                && Objects.equals(productReference, orderItem.productReference)
                && Objects.equals(unitPrice, orderItem.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productReference, quantity, unitPrice);
    }
}