package com.assignment.order_service.service;

import com.assignment.order_service.dto.CreateOrderRequest;
import com.assignment.order_service.dto.OrderResponse;
import com.assignment.order_service.entity.Order;
import com.assignment.order_service.exception.ResourceNotFoundException;
import com.assignment.order_service.kafka.OrderCreatedEvent;
import com.assignment.order_service.kafka.OrderProducer;
import com.assignment.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderProducer producer;

    public OrderService(OrderRepository repository,
                        OrderProducer producer) {

        this.repository = repository;
        this.producer = producer;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();

        order.setCustomerName(request.getCustomerName());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setStatus("CREATED");

        Order saved = repository.save(order);

        producer.publish(
                new OrderCreatedEvent(
                        saved.getId(),
                        saved.getProductId(),
                        saved.getQuantity()
                )
        );

        return new OrderResponse(
                saved.getId(),
                saved.getCustomerName(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getStatus()
        );
    }

    public OrderResponse getOrder(Long id) {

        Order order = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + id));

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus()
        );
    }
}