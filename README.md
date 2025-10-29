# Flight Aggregator API - Case Study

A Spring Boot application that aggregates flight search results from multiple SOAP providers (Provider A and Provider B) and exposes them through a unified REST API.

## Architecture Overview

This project consists of three microservices:

- **Flight Provider A** - SOAP Web Service on port **8080**
- **Flight Provider B** - SOAP Web Service on port **8081**
- **Flight Aggregator API** - REST API on port **8082**

All services run in Docker containers with a PostgreSQL database.

## Prerequisites

- **Docker & Docker Compose**

## Quick Start

Start all services with a single command:

```bash
docker compose up -d
```

This will:
1. Build Docker images for all three services
2. Start PostgreSQL database
3. Start all microservices
4. Wait for services to be healthy before starting dependent services

To view logs:
```bash
docker compose logs -f
```

## Testing the API

### Swagger UI
Open your browser and navigate to: `http://localhost:8082/swagger-ui.html`

### Example API Calls

Get all flights:
```bash
curl "http://localhost:8082/api/v1/flights/IST/COV/2026-01-01T10:00:00"
```

Get cheapest flights:
```bash
curl "http://localhost:8082/api/v1/flights/cheapest/IST/COV/2026-01-01T10:00:00"
```

View API logs:
```bash
curl "http://localhost:8082/api/v1/logs"
```

## API Endpoints

- `GET /api/v1/flights/{origin}/{destination}/{departureDate}` - Get all flights
- `GET /api/v1/flights/cheapest/{origin}/{destination}/{departureDate}` - Get cheapest flights
- `GET /api/v1/flights/provider-a?origin=X&destination=Y&departureDate=Z` - Search Provider A
- `GET /api/v1/flights/provider-b?departure=X&arrival=Y&departureDate=Z` - Search Provider B
- `GET /api/v1/logs` - View API logs

Full API documentation: `http://localhost:8082/swagger-ui.html`

## Stopping Services

```bash
docker compose down
```

To remove volumes (database data):
```bash
docker compose down -v
```
