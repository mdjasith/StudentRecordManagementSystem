# Student Management System - Spring Boot Backend

A RESTful backend for managing student records with full CRUD operations.  
Built with Spring Boot 3, JPA, MySQL, and Lombok.

---

## Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL 8
- Lombok
- Bean Validation (Jakarta)
- Maven

---

## Getting Started

### 1. Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8 running locally

### 2. Configure the Database

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password_here
```

The database `student_db` will be created automatically on first run.

### 3. Run the App

```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/students` | Register a new student |
| GET | `/api/v1/students` | Get all students (paginated) |
| GET | `/api/v1/students/{id}` | Get student by ID |
| GET | `/api/v1/students/roll/{rollNumber}` | Get by roll number |
| GET | `/api/v1/students/email/{email}` | Get by email |
| GET | `/api/v1/students/search?keyword=` | Search students |
| GET | `/api/v1/students/department/{dept}` | Get by department |
| GET | `/api/v1/students/departments` | List all departments |
| PUT | `/api/v1/students/{id}` | Full update |
| PATCH | `/api/v1/students/{id}` | Partial update |
| PATCH | `/api/v1/students/{id}/deactivate` | Soft delete / deactivate |
| PATCH | `/api/v1/students/{id}/activate` | Reactivate student |
| DELETE | `/api/v1/students/{id}` | Hard delete |

---

## Sample Request Body (POST / PUT)

```json
{
  "firstName": "Ravi",
  "lastName": "Kumar",
  "email": "ravi.kumar@college.edu",
  "phone": "9876543210",
  "dateOfBirth": "2002-05-15",
  "gender": "Male",
  "address": "123 MG Road, Bengaluru",
  "department": "Computer Science",
  "course": "B.Tech",
  "yearOfStudy": 2,
  "rollNumber": "CS2024001",
  "cgpa": 8.5
}
```

---

## Pagination & Sorting

```
GET /api/v1/students?page=0&size=10&sortBy=firstName&sortDir=asc
```

---

## Running Tests

```bash
mvn test
```

---

## Project Structure

```
src/main/java/com/studentmgmt/
├── controller/         → REST controllers
├── service/            → Service interfaces
├── serviceImpl/        → Business logic
├── repository/         → JPA repositories
├── model/              → Entity classes
├── dto/                → Request & Response DTOs
├── exception/          → Custom exceptions & global handler
└── config/             → CORS & app config
```
