# TaskFlow

A small full-stack productivity app: track daily **tasks**, a **timetable**, **goals**, and **study/work sessions**, with a dashboard that pulls today's items together.

Built with **React.js** (frontend) + **Spring Boot** (backend REST API) + **MySQL** (database).

This project intentionally follows the same layered architecture and coding style as a JSP-based full-stack project (`model` → `repository` → `service` → `controller`, constructor injection, plain POJOs, no Lombok) — the main difference is that the controllers here are `@RestController`s returning JSON instead of `ModelAndView`s, because the frontend is a separate React app instead of JSP pages.

---

## Features

1. **Tasks** — add, edit, delete tasks with title, description, priority (Low/Medium/High), and status (Pending/Completed). Filter by priority and/or status.
2. **Daily Timetable** — add time slots with a date, start time, end time, and activity name.
3. **Goals** — add daily goals with a title, date, and completion checkbox.
4. **Sessions** — log study/work sessions with subject, duration (minutes), and date.
5. **Dashboard** — one page showing today's pending tasks, today's timetable, today's goals, and today's sessions as simple cards.

---

## Project structure

```
TaskFlow/
├── database.sql                  # optional manual schema (Hibernate can also auto-create it)
├── backend/                      # Spring Boot REST API
│   ├── pom.xml
│   └── src/main/java/com/taskflow/
│       ├── TaskFlowApplication.java
│       ├── config/CorsConfig.java
│       ├── model/                # Task, TimetableSlot, Goal, StudySession
│       ├── repository/           # Spring Data JPA repositories
│       ├── service/              # business logic
│       └── controller/           # REST controllers (@RestController)
│   └── src/main/resources/application.properties
└── frontend/                     # React app
    ├── package.json
    ├── public/index.html
    └── src/
        ├── api/api.js            # axios instance
        ├── components/Navbar.js
        ├── pages/                # DashboardPage, TasksPage, TimetablePage, GoalsPage, SessionsPage
        ├── App.js, App.css, index.js
```

---

## Tech stack

- **Frontend:** React.js, React Router, Axios, plain CSS
- **Backend:** Spring Boot 3, Spring Web, Spring Data JPA
- **Database:** MySQL

There is **no authentication** — it's a single-user local app, kept deliberately simple.

---

## How the pieces connect

- The React app runs on `http://localhost:3000` and calls the API at `http://localhost:8080/api/...` using Axios (`frontend/src/api/api.js`).
- `CorsConfig.java` on the backend explicitly allows requests from `http://localhost:3000` on `/api/**`, otherwise the browser blocks the calls (CORS).
- Each feature is a vertical slice: `Model` (JPA entity) → `Repository` (Spring Data interface) → `Service` (business logic, defaults, validation) → `Controller` (REST endpoints returning JSON).
- `DashboardController` doesn't duplicate logic — it just calls the four existing services and combines their "today" results into one JSON response.

---

## Setup instructions

### 1. Database

Create the database (Spring Boot/Hibernate will create the tables automatically on first run because of `spring.jpa.hibernate.ddl-auto=update`, but you can also run `database.sql` by hand):

```sql
CREATE DATABASE taskflow;
```

### 2. Backend (Spring Boot)

Edit `backend/src/main/resources/application.properties` if your MySQL username/password are different from `root` / `root`:

```properties
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
```

Then run:

```bash
cd backend
mvn spring-boot:run
```

The API starts on **http://localhost:8080**.

### 3. Frontend (React)

```bash
cd frontend
npm install
npm start
```

The app opens on **http://localhost:3000** and talks to the backend automatically.

---

## API endpoints (for reference)

| Method | Endpoint                    | Description                          |
|--------|------------------------------|---------------------------------------|
| GET    | /api/tasks?priority=&status= | list tasks, optionally filtered       |
| POST   | /api/tasks                   | add a task                            |
| PUT    | /api/tasks/{id}               | update a task                         |
| DELETE | /api/tasks/{id}               | delete a task                         |
| GET    | /api/timetable?date=YYYY-MM-DD | list slots for a date (defaults to all if omitted) |
| GET    | /api/timetable/today          | today's slots                         |
| POST   | /api/timetable                | add a slot                            |
| PUT    | /api/timetable/{id}           | update a slot                         |
| DELETE | /api/timetable/{id}           | delete a slot                         |
| GET    | /api/goals                    | list all goals                        |
| GET    | /api/goals/today              | today's goals                         |
| POST   | /api/goals                    | add a goal                            |
| PUT    | /api/goals/{id}/toggle        | toggle completed                      |
| DELETE | /api/goals/{id}               | delete a goal                         |
| GET    | /api/sessions                 | list all sessions                     |
| GET    | /api/sessions/today           | today's sessions                      |
| POST   | /api/sessions                 | add a session                         |
| DELETE | /api/sessions/{id}            | delete a session                      |
| GET    | /api/dashboard                | today's tasks + timetable + goals + sessions in one response |

---

## How to explain this project in an interview

- **Why layered architecture?** Separating `model` / `repository` / `service` / `controller` keeps each class doing one job: entities just hold data, repositories just talk to the database (Spring Data JPA generates the SQL from method names like `findByStatus`), services hold the business rules (e.g. defaulting a new task's status to "Pending"), and controllers just translate HTTP requests into service calls and JSON responses.
- **Why REST + React instead of JSP?** JSP renders HTML on the server; a REST API lets any frontend (React here) consume the same backend as pure JSON, which is the standard pattern for modern full-stack apps and for mobile clients too.
- **How does filtering work?** `TaskController` accepts optional `priority` and `status` query params; `TaskService.getTasksByFilter` picks the right repository method depending on which params are present.
- **How does "today" work?** Timetable, Goals, and Sessions all store a date column. The service layer has a `getTodaysX()` method that queries with `LocalDate.now()`, and `DashboardController` just calls all four "today" methods and merges the results.
- **What would you add next?** Authentication (Spring Security + JWT), pagination for large lists, and validation annotations (`@NotBlank`, etc.) with proper error responses instead of relying on service-layer checks.
"# taskflow-react-redux" 
