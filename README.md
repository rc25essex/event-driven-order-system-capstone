# Event-Driven Order Processing System

## Overview

This project demonstrates an event-driven architecture using Java 21, Spring Boot and Apache Kafka. It simulates an order processing workflow where orders are created through a REST API, processed asynchronously by a fulfilment service and updated based on the fulfilment outcome.

The project was developed as a capstone project for the Advanced Object-Oriented Design and Programming module.

## Technologies

- Java 21
- Spring Boot 4
- Spring Data JPA
- Apache Kafka
- H2 Database
- Gradle
- JUnit 5
- Mockito
- Docker

## Architecture

The application consists of two logical services contained within a single Spring Boot application.

- **Order Service**
    - Creates and stores orders.
    - Publishes `OrderCreatedEvent`.
    - Updates order status after fulfilment.

- **Fulfilment Service**
    - Consumes `OrderCreatedEvent`.
    - Applies fulfilment rules.
    - Publishes either `OrderFulfilledEvent` or `OrderRejectedEvent`.

Communication between the services is performed asynchronously through Apache Kafka.

## Kafka Topics

| Topic | Description |
|-------|-------------|
| order-created | Published when a new order is created. |
| order-result | Published after fulfilment processing completes. |

## REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders` | Retrieve all orders |
| GET | `/api/orders/{id}` | Retrieve an order by ID |

## Running the Application

1. Start Docker Desktop.
2. Start Kafka.

```bash
docker compose up -d
```

3. Run the Spring Boot application.
4. Test the REST API using Postman.

## Testing

### Unit Tests

Run the JUnit 5 and Mockito test suite:

```bash
./gradlew test
```

or on Windows:

```cmd
gradlew.bat test
```

These tests verify:

- Domain model
- Service layer
- REST controllers
- Kafka producers
- Kafka consumers

### Karate API Tests

Run the Karate BDD API and integration tests:

```bash
./gradlew karateTest
```

or on Windows:

```cmd
gradlew.bat karateTest
```

These tests validate:

- REST API endpoints
- Request and response validation
- End-to-end application behaviour
- Event-driven order processing workflow

## Future Enhancements

- Inventory management
- Payment integration
- Order cancellation
- Retry and Dead Letter Queue (DLQ)
- Authentication and authorisation
- Microservice deployment using Kubernetes
