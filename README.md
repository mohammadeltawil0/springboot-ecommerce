# Spring Boot E-Commerce REST API

A backend REST API built with Spring Boot that demonstrates RESTful web services, CRUD operations, DTO mapping, pagination, validation, exception handling, and relational database management using Spring Data JPA.

## Features

- Category CRUD operations
- Product management
- Spring Data JPA
- DTO pattern
- ModelMapper
- Pagination & Sorting
- Global Exception Handling
- Bean Validation
- H2 Database

## Technologies

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Web
- H2 Database
- Maven
- ModelMapper
- Lombok

## Endpoints

### Categories

| Method | Endpoint |
|--------|----------|
| GET | `/api/public/categories` |
| POST | `/api/public/categories` |
| PUT | `/api/public/categories/{id}` |
| DELETE | `/api/public/categories/{id}` |

### Products

| Method | Endpoint |
|--------|----------|
| GET | `/api/public/products` |
| POST | `/api/admin/categories/{categoryId}/product` |

## Run

```bash
mvn spring-boot:run
```

H2 Console

```
http://localhost:8080/h2-console
```

JDBC URL

```
jdbc:h2:mem:test
```

## Future Improvements

- Spring Security
- JWT Authentication
- MySQL/PostgreSQL
- Swagger Documentation
- Unit Testing
- Shopping Cart
- Orders
