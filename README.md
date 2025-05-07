# Library Management System

A robust and secure library management system built with Spring Boot, providing comprehensive features for managing books, users, and borrowing operations.

## Project Overview

This Library Management System is a RESTful API that enables efficient management of library resources. It supports two types of users: Librarians and Patrons, each with different levels of access and permissions.

### Key Features

- **User Management**
  - User registration and authentication with JWT
  - Role-based access control (Librarian and Patron roles)
  - User profile management
  - Secure password handling with BCrypt encryption

- **Book Management**
  - Add, update, and delete books
  - Search books by title, author, ISBN, or genre
  - Track book availability and quantity
  - Real-time book availability updates using Server-Sent Events (SSE)

- **Borrowing System**
  - Borrow and return books (Max 3 Books)
  - Track borrowing history
  - Monitor overdue books
  - Automatic availability updates

- **Security Features**
  - JWT-based authentication
  - Role-based authorization
  - Secure password storage
  - Input validation and sanitization

## Technology Stack

- **Backend Framework**: Spring Boot 3.3.11
- **Language**: Java 21
- **Database**: PostgreSQL 16.3
- **ORM**: Hibernate/JPA
- **Security**: Spring Security with JWT
- **API Documentation**: OpenAPI/Swagger
- **Testing**: JUnit, Mockito
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL 16.3 (if running without Docker)

## Running the Application

### Using Docker (Recommended)

1. Clone the repository:
   ```bash
   git clone https://github.com/omrfth23/Patika.dev-Getir-Java-Spring-Boot-Bootcamp-Project.git
   cd Patika.dev-Getir-Java-Spring-Boot-Bootcamp-Project
   ```

2. Build and run using Docker Compose:
   ```bash
   docker-compose up --build
   ```

The application will be available at `http://localhost:8080`

### Running Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/omrfth23/Patika.dev-Getir-Java-Spring-Boot-Bootcamp-Project.git
   cd Patika.dev-Getir-Java-Spring-Boot-Bootcamp-Project
   ```

2. Configure PostgreSQL:
   - Create a database named `librarydb`
   - Update `application.properties` with your database credentials

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

The API documentation is available through Swagger UI at:
```
http://localhost:8080/swagger-ui/index.html
```

### Main API Endpoints

#### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

#### Books
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Add new book (Librarian only)
- `PUT /api/books/{id}` - Update book (Librarian only)
- `DELETE /api/books/{id}` - Delete book (Librarian only)
- `GET /api/books/search` - Search books with filters

#### Users
- `GET /api/users` - Get all users (Librarian only)
- `GET /api/users/{id}` - Get user by ID (Librarian only)
- `POST /api/users` - Create new user (Librarian only)
- `PUT /api/users/{id}` - Update user (Librarian only)
- `DELETE /api/users/{id}` - Delete user (Librarian only)
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update current user profile

#### Borrowing
- `POST /api/borrow` - Borrow a book
- `PUT /api/borrow/return/{id}` - Return a book
- `GET /api/borrow` - Get all borrow records (Librarian only)
- `GET /api/borrow/overdue` - Get overdue books (Librarian only)
- `DELETE /api/borrow/{id}` - Deletes a borrow record by its ID (Librarian only)

## Database Schema

The system uses the following main entities:

### Users
- id (PK)
- name
- email (unique)
- password (encrypted)
- phone (unique)
- role (LIBRARIAN/PATRON)
- registeredDate

### Books
- bookId (PK)
- title
- author
- isbn (unique)
- publicationDate
- genre
- quantity
- available

### Borrow Records
- borrowRecordId (PK)
- borrowDate
- dueDate
- returnDate
- isReturned
- userId (FK)
- bookId (FK)

## Default Users

The system initializes with the following default users:

- **Librarian**
  - Email: librarian@getir.com
  - Password: password123

- **Patron**
  - Email: patron@getir.com
  - Password: password456

## Security

- All endpoints except `/api/auth/**` and Swagger UI require authentication
- JWT tokens expire after 24 hours
- Passwords are encrypted using BCrypt
- Role-based access control for sensitive operations

## Testing

Run the test suite using:
```bash
mvn test
```

The test suite includes:
- Unit tests for services and controllers
- Integration tests with H2 in-memory database
- Security configuration tests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
