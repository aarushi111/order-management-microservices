package com.assignment.inventory_service.kafka;

import com.assignment.inventory_service.dto.OrderCreatedEvent;
import com.assignment.inventory_service.entity.Inventory;
import com.assignment.inventory_service.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedConsumer {

    private final InventoryRepository inventoryRepository;

    public OrderCreatedConsumer(
            InventoryRepository inventoryRepository) {

        this.inventoryRepository = inventoryRepository;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "inventory-group-v2"
    )
    public void consume(String message) {

        System.out.println();
        System.out.println("=================================");
        System.out.println("INVENTORY MESSAGE RECEIVED");
        System.out.println(message);
        System.out.println("=================================");
        System.out.println();
    }
}
