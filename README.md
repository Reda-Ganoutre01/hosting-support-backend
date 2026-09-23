# Hosting Support Backend

REST API backend for a hosting support platform (admin dashboard + support portal). Secured with JWT, documented with OpenAPI/Swagger, and built with Spring Boot 4.

## Features

- **Authentication & authorization** — JWT-based login and registration, BCrypt password hashing, role-based access control (ADMIN / non-admin).
- **Support ticketing** — full CRUD on tickets, messages, priorities and statuses.
- **Hosting management** — hosting plans, hosting accounts (with analytics endpoint), website orders.
- **Customer knowledge base** — FAQs and AI-generated responses linked to tickets.
- **Notification & workflow logging** — per-user notifications and audit workflow logs.
- **Contact form** — public submission (`POST /api/contacts`) plus admin management.
- **Maintenance mode** — global on/off switch with a public status endpoint so clients can show a banner; during maintenance only admins can log in and registration is disabled.
- **Data seeding** — `DataInitializer` seeds demo data (users, plans, tickets, FAQs, etc.) on startup.
- **OpenAPI / Swagger UI** — interactive API documentation.
- **CI/CD** — GitHub Actions workflow runs the full build and test suite on push/PR.

## Tech Stack

| Layer      | Technology                                   |
|------------|----------------------------------------------|
| Language   | Java 23                                      |
| Framework  | Spring Boot 4.1.0                           |
| Persistence| Spring Data JPA (Hibernate)                  |
| Database   | MySQL (runtime), H2 (tests)                  |
| Security   | Spring Security + JJWT 0.12                 |
| API Docs   | springdoc-openapi 3.x (Swagger UI)          |
| Build      | Maven                                        |
| Testing    | JUnit 5, MockMvc, Selenium, WebDriverManager |

## Getting Started

### Prerequisites

- JDK 23
- Maven 3.9+ (or use the included Maven wrapper `./mvnw`)
- MySQL 8+

### 1. Create the database

```sql
CREATE DATABASE IF NOT EXISTS hosting_support_db;
```

### 2. Configure the datasource

Edit `src/main/resources/application.yml` (defaults: `localhost:3306`, user `root`, empty password) or override via environment variables:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hosting_support_db
    username: root
    password:
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on **http://localhost:8081**.

### Swagger UI

- UI: http://localhost:8081/swagger-ui
- OpenAPI JSON: http://localhost:8081/v3/api-docs

## Configuration

| Environment variable | Default value | Description |
|----------------------|---------------|-------------|
| `JWT_SECRET`         | dev-only base64 key | Secret used to sign/verify JWT HMAC keys. **Set a strong random base64 key (≥ 32 bytes) in production.** |
| Database URL / user / password | `localhost:3306`, `root`, empty | Configured directly in `application.yml`. |

JWT expiration is `86400000` ms (24h), configurable via `app.jwt.expiration-ms`.

## Run Tests

```bash
# Unit + integration tests (Selenium excluded by default)
./mvnw test

# Build with tests
./mvnw verify

# Include Selenium end-to-end tests (needs backend on :8081 and frontend dev server on :5173)
./mvnw test -Pselenium
```

Tests use an in-memory H2 database (`MODE=MySQL`), so no MySQL server is required to run the suite.

## API Overview

All endpoints are prefixed with `/api`. Public endpoints: `/api/auth/**`, `GET /api/settings/maintenance`, `POST /api/contacts`, Swagger UI and OpenAPI docs.

| Resource               | Base path               | Endpoints |
|------------------------|-------------------------|-----------|
| Authentication         | `/api/auth`             | `POST /login`, `POST /register` |
| Users                  | `/api/users`            | `GET /`, `GET /all`, `GET /{id}`, `GET /email/{email}`, `POST /`, `PUT /{id}`, `DELETE /{id}` |
| Hosting plans          | `/api/hostingPlans`     | CRUD `GET/POST/PUT/DELETE` |
| Hosting accounts       | `/api/hostingAccounts`  | CRUD + `GET /user/{userId}`, `GET /analytics` |
| Tickets                | `/api/tickets`          | CRUD + `GET /user/{userId}` |
| Messages               | `/api/messages`         | CRUD + `GET /ticket/{ticketId}` |
| AI responses           | `/api/aiResponses`      | CRUD + `GET /ticket/{ticketId}` |
| Notifications          | `/api/notifications`    | CRUD + `GET /user/{userId}` |
| FAQs                   | `/api/faqs`             | CRUD |
| Website orders         | `/api/websiteOrders`    | CRUD + `GET /user/{userId}` |
| Workflow logs          | `/api/workflowLogs`     | CRUD |
| Contacts (form)        | `/api/contacts`         | `POST /` (public), `GET /`, `GET /{id}`, `DELETE /{id}` |
| Settings / maintenance | `/api/settings`         | `GET/PUT /maintenance` (PUT requires `ADMIN`) |

## Project Structure

```
src/main/java/hosting_support_backend
├── controller   # REST controllers (thin, delegating to services)
├── service      # Business logic + interfaces
├── repository   # Spring Data JPA repositories
├── entity       # JPA entities + enums
├── dto          # Request / response DTOs
├── security     # JWT provider, JWT filter, maintenance mode filter
├── config       # Security config, OpenAPI config, data initializer
├── exception    # Custom exceptions
└── constants    # Security constants
```

## CI/CD

The GitHub Actions workflow (`.github/workflows/ci.yml`) runs on push and pull requests to `main`:

- Checks out the repository on `ubuntu-latest`
- Sets up Temurin JDK 23 with Maven dependency caching
- Runs `./mvnw verify` (build + full test suite)

