package com.assignment.order_service.controller;

import com.assignment.order_service.dto.OrderResponse;
import com.assignment.order_service.exception.ResourceNotFoundException;
import com.assignment.order_service.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("POST /api/orders should create order")
    void createOrder_shouldReturnCreatedOrder() throws Exception {

        OrderResponse response =
                new OrderResponse(
                        1L,
                        "Aarushi",
                        "P100",
                        2,
                        "CREATED"
                );

        when(orderService.createOrder(any()))
                .thenReturn(response);

        String requestBody = """
                {
                  "customerName":"Aarushi",
                  "productId":"P100",
                  "quantity":2
                }
                """;

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Aarushi"))
                .andExpect(jsonPath("$.productId").value("P100"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("GET /api/orders/{id} should return order")
    void getOrder_shouldReturnOrder() throws Exception {

        OrderResponse response =
                new OrderResponse(
                        1L,
                        "Aarushi",
                        "P100",
                        2,
                        "CREATED"
                );

        when(orderService.getOrder(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/orders/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Aarushi"))
                .andExpect(jsonPath("$.productId").value("P100"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("GET /api/orders/{id} should return 404 when order not found")
    void getOrder_shouldReturnNotFound() throws Exception {

        when(orderService.getOrder(999L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Order not found: 999"
                        )
                );

        mockMvc.perform(
                        get("/api/orders/999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/orders should return 400 for invalid request")
    void createOrder_shouldReturnBadRequestForInvalidInput() throws Exception {

        String invalidRequestBody = """
                {
                  "customerName":"",
                  "productId":"",
                  "quantity":0
                }
                """;

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequestBody)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }
}