package com.assignment.order_service.service;

import com.assignment.order_service.dto.CreateOrderRequest;
import com.assignment.order_service.dto.OrderResponse;
import com.assignment.order_service.entity.Order;
import com.assignment.order_service.exception.ResourceNotFoundException;
import com.assignment.order_service.kafka.OrderCreatedEvent;
import com.assignment.order_service.kafka.OrderProducer;
import com.assignment.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProducer orderProducer;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                orderRepository,
                orderProducer
        );
    }

    @Test
    @DisplayName("createOrder should save order and publish Kafka event")
    void createOrder_shouldSaveOrderAndPublishEvent() {

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Aarushi");
        request.setProductId("P100");
        request.setQuantity(2);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerName("Aarushi");
        savedOrder.setProductId("P100");
        savedOrder.setQuantity(2);
        savedOrder.setStatus("CREATED");

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        OrderResponse response =
                orderService.createOrder(request);

        assertEquals(1L, response.getId());
        assertEquals("Aarushi", response.getCustomerName());
        assertEquals("P100", response.getProductId());
        assertEquals(2, response.getQuantity());
        assertEquals("CREATED", response.getStatus());

        ArgumentCaptor<Order> orderCaptor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        assertEquals("Aarushi", capturedOrder.getCustomerName());
        assertEquals("P100", capturedOrder.getProductId());
        assertEquals(2, capturedOrder.getQuantity());
        assertEquals("CREATED", capturedOrder.getStatus());

        ArgumentCaptor<OrderCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(OrderCreatedEvent.class);

        verify(orderProducer).publish(eventCaptor.capture());

        OrderCreatedEvent publishedEvent =
                eventCaptor.getValue();

        assertEquals(1L, publishedEvent.getOrderId());
        assertEquals("P100", publishedEvent.getProductId());
        assertEquals(2, publishedEvent.getQuantity());
    }

    @Test
    @DisplayName("getOrder should return order when order exists")
    void getOrder_shouldReturnOrderWhenFound() {

        Order existingOrder = new Order();
        existingOrder.setId(10L);
        existingOrder.setCustomerName("Aarushi");
        existingOrder.setProductId("P200");
        existingOrder.setQuantity(5);
        existingOrder.setStatus("CREATED");

        when(orderRepository.findById(10L))
                .thenReturn(Optional.of(existingOrder));

        OrderResponse response =
                orderService.getOrder(10L);

        assertEquals(10L, response.getId());
        assertEquals("Aarushi", response.getCustomerName());
        assertEquals("P200", response.getProductId());
        assertEquals(5, response.getQuantity());
        assertEquals("CREATED", response.getStatus());

        verify(orderRepository).findById(10L);
        verify(orderProducer, never())
                .publish(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("getOrder should throw exception when order does not exist")
    void getOrder_shouldThrowExceptionWhenNotFound() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> orderService.getOrder(999L)
                );

        assertEquals(
                "Order not found: 999",
                exception.getMessage()
        );

        verify(orderRepository).findById(999L);
        verify(orderProducer, never())
                .publish(any(OrderCreatedEvent.class));
    }
}