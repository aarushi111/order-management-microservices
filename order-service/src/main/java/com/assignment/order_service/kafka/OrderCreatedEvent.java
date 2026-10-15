package com.assignment.order_service.kafka;

public class OrderCreatedEvent {

    private Long orderId;
    private String productId;
    private Integer quantity;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId,
                             String productId,
                             Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
