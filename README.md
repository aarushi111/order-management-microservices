# Order Management Microservices

## Overview

This project demonstrates an event-driven microservices architecture using Spring Boot and Apache Kafka.

The application consists of two independent services:

1. Order Service
2. Inventory Service

When an order is created, Order Service publishes an event to Kafka. Inventory Service consumes the event asynchronously.

---

## Technologies Used

- Java 21
- Spring Boot 3
- Spring Data JPA
- Apache Kafka
- H2 Database
- Maven
- JUnit 5
- Mockito

---

## Architecture

Order Service
|
v
Kafka Topic (order-created)
|
v
Inventory Service

---

## Services

### Order Service

Port:

8085

Responsibilities:

- Create Order
- Get Order By Id
- Publish Kafka Event

### Inventory Service

Port:

8086

Responsibilities:

- Consume Kafka Events
- Process Inventory Updates

---

## Kafka Topic

order-created

---

## APIs

### Create Order

POST

http://localhost:8085/api/orders

Request:

```json
{
  "customerName": "Aarushi",
  "productId": "P100",
  "quantity": 2
}
```

Response:

```json
{
  "id": 1,
  "customerName": "Aarushi",
  "productId": "P100",
  "quantity": 2,
  "status": "CREATED"
}
```

### Get Order

GET

http://localhost:8085/api/orders/1

---

## Running the Application

### Start Kafka

```cmd
bin\windows\kafka-server-start.bat config\server.properties
```

### Start Inventory Service

```bash
mvn spring-boot:run
```

### Start Order Service

```bash
mvn spring-boot:run
```

### Test using Postman

Execute:

POST /api/orders

Inventory Service should print:

```
INVENTORY MESSAGE RECEIVED
{"orderId":1,"productId":"P100","quantity":2}
```

---

## Testing

Run:

```bash
mvn test
```

Covered:

- Create Order
- Get Order
- Order Not Found
- Validation
- Controller Tests
- Service Tests

---

## Future Improvements

- Persistent Inventory Updates
- Docker Support
- PostgreSQL Integration
- Kafka JSON Deserialization
- Integration Tests