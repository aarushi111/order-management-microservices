package com.assignment.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderCreatedEvent event) {

        System.out.println(
                "Publishing event: " + event.getOrderId()
        );

        kafkaTemplate.send(
                "order-created",
                event
        );

        System.out.println(
                "Published event successfully: "
                        + event.getOrderId()
        );
    }
}