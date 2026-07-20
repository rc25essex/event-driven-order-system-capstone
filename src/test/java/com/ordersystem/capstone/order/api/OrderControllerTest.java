package com.ordersystem.capstone.order.api;

import com.ordersystem.capstone.order.api.dto.CreateOrderRequest;
import com.ordersystem.capstone.order.api.dto.OrderItemRequest;
import com.ordersystem.capstone.order.application.OrderService;
import com.ordersystem.capstone.order.domain.Order;
import com.ordersystem.capstone.order.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    private static final String CUSTOMER_REFERENCE = "CUSTOMER-1";
    private static final String PRODUCT_REFERENCE = "PEN";
    private static final int QUANTITY = 2;
    private static final BigDecimal UNIT_PRICE = new BigDecimal("10.00");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {

        Order savedOrder = createOrder();
        CreateOrderRequest request = createOrderRequest();

        when(orderService.createOrder(any(Order.class)))
                .thenReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerReference")
                        .value(CUSTOMER_REFERENCE))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }

    private Order createOrder() {
        return new Order(
                CUSTOMER_REFERENCE,
                List.of(
                        new OrderItem(
                                PRODUCT_REFERENCE,
                                QUANTITY,
                                UNIT_PRICE
                        )
                )
        );
    }

    private CreateOrderRequest createOrderRequest() {
        return new CreateOrderRequest(
                CUSTOMER_REFERENCE,
                List.of(
                        new OrderItemRequest(
                                PRODUCT_REFERENCE,
                                QUANTITY,
                                UNIT_PRICE
                        )
                )
        );
    }
}