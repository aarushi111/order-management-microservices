package com.assignment.order_service.dto;

public class OrderResponse {

    private Long id;
    private String customerName;
    private String productId;
    private Integer quantity;
    private String status;

    public OrderResponse() {
    }
    public OrderResponse(Long id,
                         String customerName,
                         String productId,
                         Integer quantity,
                         String status) {
        this.id = id;
        this.customerName = customerName;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
