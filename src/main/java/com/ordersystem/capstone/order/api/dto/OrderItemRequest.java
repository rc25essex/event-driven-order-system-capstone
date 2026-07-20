package com.ordersystem.capstone.order.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequest(

        @NotBlank(message = "Product reference must not be blank.")
        String productReference,

        @Min(value = 1, message = "Quantity must be greater than zero.")
        int quantity,

        @NotNull(message = "Unit price must be provided.")
        @DecimalMin(
                value = "0.01",
                message = "Unit price must be greater than zero."
        )
        BigDecimal unitPrice
) {
}