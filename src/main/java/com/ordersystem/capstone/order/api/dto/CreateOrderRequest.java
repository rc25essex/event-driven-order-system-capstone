package com.ordersystem.capstone.order.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "Customer reference must not be blank.")
        String customerReference,

        @NotEmpty(message = "An order must contain at least one item.")
        List<@Valid OrderItemRequest> items
) {
}