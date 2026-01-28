# SimpleSplit - Modern Shared Expense Manager

SimpleSplit has been modernized into a full-stack web application using **Spring Boot** for the backend and **React** for the frontend.

## Tech Stack
-   **Backend**: Java 17+, Spring Boot 3.x, Spring Data JPA, H2 (Dev) / MySQL (Prod).
-   **Frontend**: React, Vite, Axios, Modern CSS.

## Prerequisites
-   Java 17 or higher
-   Maven
-   Node.js & npm

## How to Run

### 1. Backend (Spring Boot)
The backend runs on port `8080`.

```bash
# From the root directory
mvn spring-boot:run
```

Alternatively, you can build and run the JAR:
```bash
mvn clean package -DskipTests
java -jar target/SimpleSplit-1.0-SNAPSHOT.jar
```

*Note: By default, it uses an in-memory H2 database. Data will be lost on restart. To use MySQL, update `src/main/resources/application.properties`.*

### 2. Frontend (React)
The frontend runs on port `5173` (default for Vite).

```bash
# Open a new terminal
cd frontend

# Install dependencies (first time only)
npm install

# Start the development server
npm run dev
```

### 3. Usage
Once both servers are running, open your browser and navigate to:
**http://localhost:5173**

## Features
-   **Dashboard**: View current balances (who owes who).
-   **User Management**: Add new friends to the group.
-   **Add Expense**: Record a transaction and split it among selected friends.
