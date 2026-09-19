-- TaskFlow database schema
-- Spring Boot (spring.jpa.hibernate.ddl-auto=update) will actually create/update
-- these tables automatically on startup, so running this file is optional.
-- It's included so you can create the schema by hand and explain it in interviews.

CREATE DATABASE IF NOT EXISTS taskflow;
USE taskflow;

CREATE TABLE IF NOT EXISTS tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    priority VARCHAR(20) DEFAULT 'Medium',
    status VARCHAR(20) DEFAULT 'Pending'
);

CREATE TABLE IF NOT EXISTS timetable_slots (
    slot_id INT AUTO_INCREMENT PRIMARY KEY,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    activity_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS goals (
    goal_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    goal_date DATE NOT NULL,
    completed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS sessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    duration_minutes INT NOT NULL,
    session_date DATE NOT NULL
);
