# ProjectSync - Project Management Web Application

**ProjectSync** is a production-minded Spring Boot MVC web application for registering and tracking active projects across teams. The application provides a complete CRUD interface backed by MySQL persistence, with validation, custom exception handling, and a minimal Tailwind-based frontend.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [API Examples](#api-examples)
- [Frontend Usage](#frontend-usage)
- [Error Handling](#error-handling)
- [Development Notes](#development-notes)

---

## Features

✅ **Complete CRUD Operations** - Create, read, update, and delete projects  
✅ **Search & Filter** - Search by name/description and filter by status  
✅ **Validation** - Bean validation on all input with meaningful error messages  
✅ **Custom Exception Handling** - Global exception handler with HTTP error mapping  
✅ **Layered Architecture** - Separation of concerns across controllers, services, repositories  
✅ **DTOs** - Request/response mapping without exposing JPA entities  
✅ **MySQL Persistence** - Data persisted with JPA/Hibernate  
✅ **RESTful API** - Clean REST endpoints with proper HTTP methods and status codes  
✅ **Minimal Frontend** - Vanilla JavaScript with Tailwind CSS, no SPA frameworks  
✅ **Modular Code** - Single responsibility principle throughout  

---

## Architecture

ProjectSync follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                        │
│              (Frontend: HTML, CSS, JavaScript, UI)               │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP REST
┌──────────────────────────▼──────────────────────────────────────┐
│                      CONTROLLER LAYER                            │
│   (ProjectController: Request routing, HTTP method mapping)      │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                      SERVICE LAYER                               │
│  (ProjectService: Business logic, validation, mapping, DTOs)     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                   REPOSITORY LAYER                               │
│   (ProjectRepository: JPA queries, database access)              │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                      DATA LAYER                                  │
│            (MySQL Database with Hibernate ORM)                   │
└─────────────────────────────────────────────────────────────────┘
```

### Key Design Patterns

- **DTO Pattern**: Separates API contracts from domain entities
- **Service Pattern**: Encapsulates business logic and validation
- **Repository Pattern**: Abstracts data access operations
- **Exception Handler Pattern**: Centralized error handling with `@ControllerAdvice`
- **Dependency Injection**: Constructor injection for loose coupling

---

## Technology Stack

- **Backend Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **ORM**: Hibernate (via Spring Data JPA)
- **Database**: MySQL 8.0
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven
- **Frontend**: HTML5, Vanilla JavaScript (ES6+), Tailwind CSS
- **Dependency Injection**: Spring Context
- **Project Utilities**: Lombok (reduces boilerplate)

---

## Project Structure

```
ProjectSync/
├── src/
│   ├── main/
│   │   ├── java/com/example/ProjectSync/
│   │   │   ├── controllers/
│   │   │   │   └── ProjectController.java
│   │   │   ├── services/
│   │   │   │   ├── ProjectService.java
│   │   │   │   └── ProjectServiceImpl.java
│   │   │   ├── repositories/
│   │   │   │   └── ProjectRepository.java
│   │   │   ├── models/
│   │   │   │   ├── entities/
│   │   │   │   │   └── Project.java
│   │   │   │   └── dtos/
│   │   │   │       ├── CreateProjectDTO.java
│   │   │   │       ├── UpdateProjectDTO.java
│   │   │   │       └── ProjectResponseDTO.java
│   │   │   ├── util/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── exceptions/
│   │   │   │       ├── ResourceNotFoundException.java
│   │   │   │       ├── BadRequestException.java
│   │   │   │       └── DatabaseException.java
│   │   │   └── ProjectSyncApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   ├── app.js
│   │       │   └── styles.css
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/example/ProjectSync/
│           └── ProjectSyncApplicationTests.java
├── pom.xml
├── schema.sql
└── README.md
```

---

## Prerequisites

- **Java 21** or higher
- **Maven 3.6.0** or higher
- **MySQL 8.0** or higher
- **Git** (optional)

### Installation Verification

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check MySQL version
mysql --version
```

---

## Installation & Setup

### 1. Clone or Extract Project

```bash
# If using Git
git clone <repository-url>
cd ProjectSync

# Or extract the ZIP file and navigate to the project directory
```

### 2. Install Maven Dependencies

```bash
# Navigate to project root (where pom.xml is located)
cd ProjectSync

# Download all dependencies
mvn clean install

# This will:
# - Download Spring Boot and all dependencies
# - Compile the Java source code
# - Run tests (if any)
# - Create the application package
```

---

## Database Configuration

### 1. Start MySQL Server

```bash
# On Windows (if MySQL is installed as a service)
# MySQL should start automatically, or:
net start MySQL80

# On macOS
brew services start mysql

# On Linux
sudo systemctl start mysql
```

### 2. Create Database and Tables

```bash
# Open MySQL client
mysql -u root -p

# Enter MySQL shell, then execute the schema:
```

```sql
-- Option 1: Copy and paste the entire schema.sql content into MySQL shell
SOURCE schema.sql;

-- Or manually execute:
CREATE DATABASE IF NOT EXISTS projectsync_db;
USE projectsync_db;

CREATE TABLE projects (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    team VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_team (team),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 3. Verify Database Connection

Update `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/projectsync_db
spring.datasource.username=root
spring.datasource.password=
```

**Note**: Adjust the `url`, `username`, and `password` based on your MySQL setup.

---

## Running the Application

### Option 1: Run with Maven

```bash
# From the project root directory
mvn spring-boot:run

# The application will start on http://localhost:8080
```

### Option 2: Run the JAR File

```bash
# Build the project
mvn clean package

# Run the generated JAR
java -jar target/ProjectSync-0.0.1-SNAPSHOT.jar
```

### Option 3: Run from IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code with Spring Boot extension)
2. Right-click on `ProjectSyncApplication.java`
3. Select "Run as Spring Boot App"

### Access the Application

- **Frontend**: [http://localhost:8080](http://localhost:8080)
- **API Base URL**: [http://localhost:8080/api/projects](http://localhost:8080/api/projects)

---

## API Endpoints

### Base URL
```
http://localhost:8080/api/projects
```

### Endpoints Summary

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|------------|
| GET | `/api/projects` | List all projects | 200 |
| GET | `/api/projects?status=PENDING` | Filter by status | 200 |
| GET | `/api/projects?q=search` | Search projects | 200 |
| GET | `/api/projects/{id}` | Get project by ID | 200, 404 |
| POST | `/api/projects` | Create new project | 201, 400 |
| PUT | `/api/projects/{id}` | Update project | 200, 400, 404 |
| DELETE | `/api/projects/{id}` | Delete project | 204, 404 |

### Status Codes

- **200 OK** - Request successful
- **201 Created** - Resource successfully created
- **204 No Content** - Request successful, no content to return
- **400 Bad Request** - Validation error or invalid input
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

---

## API Examples

### 1. Get All Projects

```bash
curl -X GET http://localhost:8080/api/projects

# Response (200 OK)
[
  {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete redesign of company website with modern UI and responsive design",
    "status": "IN_PROGRESS",
    "team": "Frontend Team",
    "createdAt": "2025-10-27T10:30:00",
    "updatedAt": "2025-10-27T10:30:00"
  },
  {
    "id": 2,
    "name": "Mobile App Development",
    "description": "Build cross-platform mobile application for customer engagement",
    "status": "PENDING",
    "team": "Mobile Team",
    "createdAt": "2025-10-27T10:35:00",
    "updatedAt": "2025-10-27T10:35:00"
  }
]
```

### 2. Filter Projects by Status

```bash
curl -X GET "http://localhost:8080/api/projects?status=IN_PROGRESS"

# Response (200 OK)
[
  {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete redesign of company website with modern UI and responsive design",
    "status": "IN_PROGRESS",
    "team": "Frontend Team",
    "createdAt": "2025-10-27T10:30:00",
    "updatedAt": "2025-10-27T10:30:00"
  }
]
```

### 3. Search Projects

```bash
curl -X GET "http://localhost:8080/api/projects?q=database"

# Response (200 OK)
[
  {
    "id": 3,
    "name": "Database Migration",
    "description": "Migrate legacy database to cloud-based MySQL infrastructure",
    "status": "COMPLETED",
    "team": "Backend Team",
    "createdAt": "2025-10-27T10:40:00",
    "updatedAt": "2025-10-27T10:40:00"
  }
]
```

### 4. Get Project by ID

```bash
curl -X GET http://localhost:8080/api/projects/1

# Response (200 OK)
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Complete redesign of company website with modern UI and responsive design",
  "status": "IN_PROGRESS",
  "team": "Frontend Team",
  "createdAt": "2025-10-27T10:30:00",
  "updatedAt": "2025-10-27T10:30:00"
}

# If project not found (404 Not Found)
{
  "status": 404,
  "message": "Project not found with ID: 999",
  "timestamp": "2025-10-27T10:45:00"
}
```

### 5. Create New Project

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "API Gateway Implementation",
    "description": "Implement centralized API gateway for microservices architecture and request routing",
    "status": "PENDING",
    "team": "DevOps Team"
  }'

# Response (201 Created)
{
  "id": 6,
  "name": "API Gateway Implementation",
  "description": "Implement centralized API gateway for microservices architecture and request routing",
  "status": "PENDING",
  "team": "DevOps Team",
  "createdAt": "2025-10-27T11:00:00",
  "updatedAt": "2025-10-27T11:00:00"
}

# Validation Error (400 Bad Request)
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-10-27T11:05:00",
  "errors": {
    "name": "Project name is required",
    "description": "Description must be between 10 and 500 characters"
  }
}
```

### 6. Update Project

```bash
curl -X PUT http://localhost:8080/api/projects/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Website Redesign - Phase 2",
    "description": "Continue with backend optimization and database restructuring for improved performance",
    "status": "IN_PROGRESS",
    "team": "Full Stack Team"
  }'

# Response (200 OK)
{
  "id": 1,
  "name": "Website Redesign - Phase 2",
  "description": "Continue with backend optimization and database restructuring for improved performance",
  "status": "IN_PROGRESS",
  "team": "Full Stack Team",
  "createdAt": "2025-10-27T10:30:00",
  "updatedAt": "2025-10-27T11:10:00"
}
```

### 7. Delete Project

```bash
curl -X DELETE http://localhost:8080/api/projects/1

# Response (204 No Content)
# No response body

# If project not found (404 Not Found)
{
  "status": 404,
  "message": "Project not found with ID: 999",
  "timestamp": "2025-10-27T11:15:00"
}
```

---

## Frontend Usage

### Accessing the UI

1. Start the application
2. Open [http://localhost:8080](http://localhost:8080) in your browser
3. The main dashboard displays all projects

### Features

#### 1. View All Projects
- Projects are displayed as cards with name, description, status, and team
- Each card shows the last updated date
- Status is color-coded (Pending: Yellow, In Progress: Blue, Completed: Green)

#### 2. Search Projects
- Type in the "Search Projects" box to search by name or description
- Results update as you type (with 300ms debounce for performance)

#### 3. Filter by Status
- Use the "Filter by Status" dropdown to show only projects with a specific status
- "All Statuses" option shows all projects

#### 4. Create Project
- Click "New Project" button
- Fill in the form fields:
  - **Name**: 3-100 characters
  - **Description**: 10-500 characters
  - **Status**: Select from PENDING, IN_PROGRESS, COMPLETED
  - **Team**: 2-50 characters
- Click "Save Project"
- Validation errors are displayed inline

#### 5. Edit Project
- Click "Edit" button on any project card
- Modify any field
- Click "Save Project" to update
- Form validation ensures data integrity

#### 6. Delete Project
- Click "Delete" button on any project card
- Confirm deletion in the modal
- Project is permanently removed from the database

#### 7. Refresh
- Click "Refresh" button to reload all projects from the server

### Keyboard Shortcuts
- Escape: Close any open modal
- Tab: Navigate through form fields

---

## Error Handling

### Custom Exceptions

**1. ResourceNotFoundException (404)**
```java
// Thrown when a resource cannot be found
throw new ResourceNotFoundException("Project not found with ID: " + id);

// Response:
{
  "status": 404,
  "message": "Project not found with ID: 999",
  "timestamp": "2025-10-27T10:45:00"
}
```

**2. BadRequestException (400)**
```java
// Thrown when request data is invalid
throw new BadRequestException("Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED");

// Response:
{
  "status": 400,
  "message": "Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED",
  "timestamp": "2025-10-27T10:50:00"
}
```

**3. DatabaseException (500)**
```java
// Thrown when database operations fail
throw new DatabaseException("Failed to create project: " + ex.getMessage());

// Response:
{
  "status": 500,
  "message": "Database operation failed: Failed to create project: ...",
  "timestamp": "2025-10-27T10:55:00"
}
```

### Validation Errors (400)

When validation fails, the response includes field-specific error messages:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-10-27T11:00:00",
  "errors": {
    "name": "Project name must be between 3 and 100 characters",
    "description": "Description must be between 10 and 500 characters",
    "status": "Status is required",
    "team": "Team name is required"
  }
}
```

---

## Development Notes

### Code Organization

- **Single Responsibility Principle**: Each class has one reason to change
- **Dependency Injection**: All dependencies are injected via constructors
- **DTOs vs Entities**: API contracts use DTOs; entities are for persistence only
- **Transaction Management**: Service methods are transactional with proper scope (read-only where applicable)

### Key Features

1. **Validation**
   - Bean validation annotations on DTOs
   - Custom status validation in service
   - Global validation error handler

2. **Exception Handling**
   - Centralized `@ControllerAdvice` handler
   - Meaningful error messages
   - Proper HTTP status codes

3. **Database Indexing**
   - Indexes on `status`, `team`, and `created_at` for fast queries
   - Auto-timestamp fields for audit trail

4. **CORS Configuration**
   - Frontend and backend can run on different ports during development
   - `@CrossOrigin` allows cross-origin requests

### Testing

Currently, basic test class exists at:
```
src/test/java/com/example/ProjectSync/ProjectSyncApplicationTests.java
```

To add more tests, follow the Spring Boot testing conventions with `@SpringBootTest` and `@Transactional`.

### Future Enhancements

- Authentication & Authorization
- User audit logging
- Advanced search with pagination
- File attachment support
- Project templates
- Team management
- Activity history
- Email notifications

---

## Troubleshooting

### Issue: "Access denied for user 'root'@'localhost'"

**Solution**: Update MySQL credentials in `application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Issue: "Database 'projectsync_db' doesn't exist"

**Solution**: Run the schema.sql script first:
```bash
mysql -u root -p < schema.sql
```

### Issue: "Port 8080 already in use"

**Solution**: Change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: "Lombok not working"

**Solution**: 
1. In IDE, install Lombok plugin
2. Enable annotation processing: Settings → Compiler → Annotation Processors → Enable annotation processing

### Issue: "Module not found" errors when building

**Solution**: Clear Maven cache and reinstall:
```bash
mvn clean install -U
```

---

## Contact & Support

For questions or issues, please refer to the Spring Boot documentation at [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot).

---

**ProjectSync v1.0** | Built with Spring Boot 3.5.7 | © 2025
