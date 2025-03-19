# Banking Transaction Demo

A Spring Boot microservice that handles banking transactions with support for transfers, deposits, and withdrawals.

## Features

- Transaction management (transfer, deposit, withdraw)
- Pessimistic locking for concurrent transaction handling
- Account balance tracking
- Transaction history querying
- In-memory H2 database
- REST API with proper error handling
- Caching support
- Containerized deployment

## Technology Stack

- Java 21
- Spring Boot 3.4
- Spring Data JPA
- H2 Database
- Maven
- Docker
- Kubernetes

## Getting Started

### Prerequisites

- JDK 21
- Maven 3.x
- Docker (optional)
- Kubernetes cluster (optional)

### Local Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/thgeek24/transaction-demo.git
    cd transaction-demo
    ```
2. Build the application:
    ```bash
    mvn clean install
    ```
3. Run the application:
    ```bash
    mvn spring-boot:run
    ```
The application will start on http://localhost:8080

### Docker Setup

1. Build the Docker image:
    ```bash
   docker build -t banking-transaction:1.0 .
   ```
2. Run the container:
    ```bash
    docker run -p 8080:8080 banking-transaction:1.0
   ```
   
### Kubernetes Deployment (Minikube)

1. Apply the Kubernetes configuration:
    ```bash
   kubectl apply -f k8s-transaction.yaml
   ```
2. Access the service:
    ```bash
    # Get Minikube IP
    minikube ip
    # Access the service using Minikube IP
    curl http://$(minikube ip):30100/api/transactions
   ```

## Running Tests

### Execute all tests:
```bash
mvn test
```
   
### Run specific test class:
```bash
mvn test -Dtest=TransactionServiceImplTest
```

## API Usage Guide

### Endpoint: POST /api/transactions

### Example Request:
```json
{
  "trxReferenceNo": "trx111",
  "fromAccountNo":"ACC001",
  "toAccountNo":"ACC002",
  "amount": 1,
  "type": "TRANSFER",
  "description": "Sample transaction"
}
```

### Example Response:
```json
{
  "id": 1,
  "trxReferenceNo": "trx111",
  "amount": 1,
  "fromAccountNo": "ACC001",
  "toAccountNo": "ACC002",
  "status": "COMPLETED",
  "type": "TRANSFER",
  "description": "Sample transaction",
  "failedReason": null,
  "deleted": false,
  "createdAt": "2025-03-19T17:37:46.168815",
  "updatedAt": "2025-03-19T17:37:46.209181"
}

```
🔜 Updates Coming Soon