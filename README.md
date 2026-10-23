# Fast Appointment – Backend

REST API for booking appointments with service businesses (hair salons, barbers, ...).
Handles accounts, businesses, services, staff, available slots and bookings.

## Tech stack

- Java 21, Spring Boot 4
- Spring Security (JWT), Spring Data JPA
- PostgreSQL 17
- Maven

## Requirements

- JDK 21
- Maven
- Docker (for PostgreSQL)

## Setup

Create a `.env` file in the project root:

```
POSTGRES_DB=fast_appointment
POSTGRES_USER=your_user
POSTGRES_PASSWORD=your_password

DB_URL=jdbc:postgresql://localhost:5432/fast_appointment
DB_USERNAME=your_user
DB_PASSWORD=your_password

JWT_SECRET=your_base64_secret
```

Generate a JWT secret with `openssl rand -base64 32`.

Start the database:

```bash
docker compose up -d
```

## Run

```bash
mvn spring-boot:run
```

The API runs on http://localhost:8080.

The frontend origin `http://localhost:5173` is allowed by default.
Set `CORS_ALLOWED_ORIGINS` to change it.
