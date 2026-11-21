# JobTrack — Job Application Tracker REST API

A RESTful backend application built with Java 21 and Spring Boot to manage job applications and company information.

## Overview

JobTrack helps users organize and track their job applications, maintain company details, and monitor application statuses through REST APIs.

This project demonstrates backend development concepts including REST API design, layered architecture, database relationships, validation, exception handling, pagination, filtering, and automated testing.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **API Documentation:** Swagger UI / OpenAPI
- **Testing:** JUnit 5, Mockito, MockMvc

## Features

### Company Management
- Create, retrieve, update, partially update, and delete company records.
- Manage company name, website, and location.

### Job Application Management
- Create and manage job applications.
- Associate job applications with companies.
- Track application statuses:
    - APPLIED
    - INTERVIEW
    - OFFERED
    - REJECTED
    - WITHDRAWN
- Update application details using PUT and PATCH.

### Additional Backend Features
- DTO-based request and response handling.
- Request validation.
- Centralized global exception handling.
- Pagination and sorting.
- Filtering by application status, company, and application age.
- Input validation for pagination and sorting parameters.
- Swagger/OpenAPI documentation.

### Testing
- Unit tests using JUnit 5 and Mockito.
- Controller tests using MockMvc.
- Integration tests for API and database operations.

## Project Structure

```text
src/
 ├── main/
 │   ├── java/com/yash/jobtracker/
 │   │   ├── controller/
 │   │   ├── dto/
 │   │   ├── entity/
 │   │   ├── exception/
 │   │   ├── repository/
 │   │   └── service/
 │   └── resources/
 │       └── application.properties
 │
 └── test/
     ├── java/com/yash/jobtracker/
     │   ├── controller/
     │   ├── integration/
     │   └── service/
     └── resources/
         └── application-test.properties
```

## Getting Started

### Prerequisites

Make sure you have installed:

- Java 21 or later
- PostgreSQL
- Git

### 1. Clone the repository

```bash
git clone https://github.com/yashpatil-7580/JobTrack.git
cd JobTrack
```

### 2. Create the PostgreSQL database

Create a database named:

```sql
CREATE DATABASE jobtrack_db;
```

### 3. Configure database credentials

Configure your PostgreSQL connection details in `src/main/resources/application.properties`.

The database password is read through an environment variable:

```properties
spring.datasource.password=${DB_PASSWORD}
```

Set the `DB_PASSWORD` environment variable to your own PostgreSQL password. Never commit actual database credentials.

### 4. Run the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs locally at:

`http://localhost:8081`

### 5. Access Swagger UI

Once the application starts, open:

http://localhost:8081/swagger-ui/index.html

OpenAPI specification:

http://localhost:8081/v3/api-docs

Use Swagger UI to explore and test the available REST endpoints.

### 6. Run tests

On Windows:

```powershell
.\mvnw.cmd test
```

## Future Enhancements

- Spring Security integration.
- JWT-based authentication and authorization.
- User-specific job application management.
- Additional features and improvements.
- Cloud deployment.

## Repository

GitHub: https://github.com/yashpatil-7580/JobTrack

---

**Project Status:** Backend core implementation completed. Further enhancements are planned.