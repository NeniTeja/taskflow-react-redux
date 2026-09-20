# TaskFlow - Task & Productivity Management System

A full-stack productivity app to track daily **tasks**, a **timetable**, **goals**, **study/work sessions**, and **subjects**, complete with an interactive dashboard that pulls today's items together.

Built with **React.js & Redux** (frontend state management) + **Spring Boot** (backend REST API) + **JUnit 5 & Mockito** (unit testing) + **MySQL** (database).

---

## Features

1. **Tasks** — add, edit, delete tasks with title, description, priority (`Low`/`Medium`/`High`), and status (`Pending`/`Completed`). Filter by priority and/or status.
2. **Daily Timetable** — add time slots with a date, start time, end time, and activity name.
3. **Goals** — add daily goals with a title, date, and completion toggle.
4. **Sessions** — log study/work sessions with subject, duration (minutes), and date.
5. **Subjects** — manage subject lists powered by Redux centralized state management.
6. **Dashboard** — one unified dashboard showing today's pending tasks, today's timetable slots, today's goals, and today's sessions as responsive cards.
7. **Comprehensive Unit Testing** — fully tested backend service and controller layers using JUnit 5 and Mockito.

---

## Project Structure

```
TaskFlow/
├── database.sql                  # MySQL database schema script
├── backend/                      # Spring Boot REST API
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/taskflow/
│       │   ├── TaskFlowApplication.java
│       │   ├── config/CorsConfig.java
│       │   ├── model/            # Task, TimetableSlot, Goal, StudySession
│       │   ├── repository/       # Spring Data JPA repositories
│       │   ├── service/          # Business logic layer
│       │   └── controller/       # REST controllers (@RestController)
│       │   └── resources/application.properties
│       └── test/java/com/taskflow/ # JUnit 5 & Mockito Unit Tests
│           ├── controller/       # WebMvcTest controller tests (MockMvc)
│           └── service/          # Mockito service unit tests
└── frontend/                     # React + Redux app
    ├── package.json
    ├── public/index.html
    └── src/
        ├── api/api.js            # Axios instance configuration
        ├── components/Navbar.js
        ├── pages/                # DashboardPage, TasksPage, TimetablePage, GoalsPage, SessionsPage, SubjectsPage
        ├── redux/                # Redux state management (Store, Reducers, Actions, ActionTypes)
        └── App.js, App.css, index.js
```

---

## Tech Stack

- **Frontend:** React.js, Redux, React-Redux, React Router, Axios, CSS3
- **Backend:** Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Hibernate
- **Testing:** JUnit 5, Mockito, Spring Boot Test (`@WebMvcTest`, `@MockBean`, `MockMvc`)
- **Database:** MySQL

---

## How the Pieces Connect

- **Frontend State & API:** The React frontend uses **Redux** for centralized state management and calls the backend API at `http://localhost:8080/api/...` using Axios.
- **CORS Configuration:** `CorsConfig.java` on the backend allows requests from `http://localhost:3000` on `/api/**`.
- **Layered Architecture:** Follows clean separation of concerns:
  - `Model` (JPA Entity) → `Repository` (Spring Data JPA) → `Service` (Business logic) → `Controller` (REST API JSON responses).
- **Dashboard Integration:** `DashboardController` calls the four existing services to combine today's data into a single aggregated JSON payload.
- **Unit Testing:** Services are tested in isolation using Mockito mocks (`@ExtendWith(MockitoExtension.class)`). Controllers are tested using `@WebMvcTest` and `MockMvc` to verify HTTP status codes, request routing, and JSON responses.

---

## Setup Instructions

### 1. Database Setup

Create the MySQL database (Hibernate automatically creates tables on first run via `spring.jpa.hibernate.ddl-auto=update`, or execute `database.sql` manually):

```sql
CREATE DATABASE taskflow;
```

### 2. Backend (Spring Boot)

Configure `backend/src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
```

Run the backend:

```bash
cd backend
mvn spring-boot:run
```

The Spring Boot REST API will start on **http://localhost:8080**.

### 3. Running Unit Tests

Run all JUnit 5 unit tests for services and controllers:

```bash
cd backend
mvn test
```

### 4. Frontend (React + Redux)

```bash
cd frontend
npm install
npm start
```

The React app will open on **http://localhost:3000**.

---

## API Endpoints Reference

| Method | Endpoint                    | Description                                        |
|--------|------------------------------|----------------------------------------------------|
| GET    | `/api/tasks?priority=&status=` | List tasks, optionally filtered by priority/status |
| POST   | `/api/tasks`                 | Add a new task                                     |
| PUT    | `/api/tasks/{id}`             | Update a task                                      |
| DELETE | `/api/tasks/{id}`             | Delete a task                                      |
| GET    | `/api/timetable?date=YYYY-MM-DD` | List timetable slots for a date                  |
| GET    | `/api/timetable/today`        | Fetch today's timetable slots                      |
| POST   | `/api/timetable`              | Add a new slot                                     |
| PUT    | `/api/timetable/{id}`         | Update a slot                                      |
| DELETE | `/api/timetable/{id}`         | Delete a slot                                      |
| GET    | `/api/goals`                  | List all goals                                     |
| GET    | `/api/goals/today`            | Fetch today's goals                                |
| POST   | `/api/goals`                  | Add a new goal                                     |
| PUT    | `/api/goals/{id}/toggle`      | Toggle goal completion status                      |
| DELETE | `/api/goals/{id}`             | Delete a goal                                      |
| GET    | `/api/sessions`               | List all study sessions                            |
| GET    | `/api/sessions/today`         | Fetch today's study sessions                       |
| POST   | `/api/sessions`               | Add a new study session                            |
| DELETE | `/api/sessions/{id}`          | Delete a study session                             |
| GET    | `/api/dashboard`              | Aggregate today's tasks, slots, goals, & sessions  |

---

## How to Explain This Project in an Interview

- **Why Layered Architecture?** `Model` / `Repository` / `Service` / `Controller` separation keeps logic decoupled and easy to unit test.
- **Why REST + React & Redux?** REST APIs decouple backend services from client applications. Redux provides predictable state management for complex UI components.
- **How is Unit Testing Implemented?** Service classes are unit tested with Mockito to verify business logic and repository interactions without hitting a real database. Controller endpoints are verified using Spring's `@WebMvcTest` and `MockMvc` to test request mapping, response serialization, and status codes.
