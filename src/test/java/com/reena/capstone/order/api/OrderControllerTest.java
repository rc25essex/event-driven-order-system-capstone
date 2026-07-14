package com.reena.capstone.order.api;

import tools.jackson.databind.ObjectMapper;
import com.reena.capstone.order.application.OrderService;
import com.reena.capstone.order.domain.Order;
import com.reena.capstone.order.domain.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {

        Order order = new Order(
                "CUSTOMER-1",
                List.of(
                        new OrderItem(
                                "PEN",
                                2,
                                new BigDecimal("10.00")
                        )
                )
        );

        when(orderService.createOrder(any(Order.class)))
                .thenReturn(order);

        String request = """
                {
                  "customerReference": "CUSTOMER-1",
                  "items": [
                    {
                      "productReference": "PEN",
                      "quantity": 2,
                      "unitPrice": 10.00
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerReference")
                        .value("CUSTOMER-1"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }
}