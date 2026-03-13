# Secure Online Examination System

## Project Overview
The **Secure Online Examination System** is a robust backend REST API built using **Spring Boot**. It provides a fully secured, role-based platform for managing and conducting online examinations, featuring advanced user management and administrative location integration.

## Key Features
*   **Secure User Management:** Registration and management of users with role-based access control.
*   **Exam Management:** Create, update, delete, and search for exams.
*   **Enrollment System:** Securely enroll students in specific exams (Many-to-Many relationship).
*   **Location Hierarchy:** Integration of Rwanda's administrative structure (Province → District → Sector → Cell → Village).
*   **Automated Location Resolution:** Automatically resolves higher-level administrative entities based on the village selection.
*   **Advanced Querying:** Paginated, sorted, and filtered API responses for users and exams.
*   **Postman Support:** Includes a comprehensive Postman collection for API testing.

## Technical Stack
*   **Language:** Java 21
*   **Framework:** Spring Boot 4.0.3
*   **Database:** PostgreSQL
*   **ORM:** Spring Data JPA / Hibernate
*   **Build Tool:** Maven
*   **Utilities:** Lombok, Spring Boot Validation

## Project Structure
```text
src/main/java/SecureOnlineExamination/Secure_Online_Examination_System/
├── config        # Configuration classes
├── controller    # REST API Controllers (User, Exam, Location)
├── dto           # Data Transfer Objects for API requests
├── model         # Entity models (User, Exam, Location, etc.)
├── repository    # JPA Repositories
└── service       # Business logic implementation
```

## Getting Started

### Prerequisites
*   Java 21 or higher
*   Maven 3.6+
*   PostgreSQL 15+

### Database Setup
1. Create a PostgreSQL database named `exam_system_db`.
2. Update the `src/main/resources/application.properties` file with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/exam_system_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Installation and Running
1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on port `8090` by default.

## API Documentation
Testing and exploring the API is made easy with the included Postman collection:
*   [Online-Examination-System.postman_collection.json](Online-Examination-System.postman_collection.json)

Import this file into Postman to see all available endpoints, including:
*   **User Endpoints:** Registration, Role-based searching, Location-based filtering.
*   **Exam Endpoints:** Creation, Enrollment, Activation search.
*   **Location Endpoints:** Hierarchy exploration, Village lookup.

## Authors
*   **Ishimwe Marie Joyeuse**

---
*Developed as part of the Web Tech Midterm Examination.*
