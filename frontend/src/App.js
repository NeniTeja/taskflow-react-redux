import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import DashboardPage from "./pages/DashboardPage";
import TasksPage from "./pages/TasksPage";
import TimetablePage from "./pages/TimetablePage";
import GoalsPage from "./pages/GoalsPage";
import SessionsPage from "./pages/SessionsPage";
import SubjectsPage from "./pages/SubjectsPage";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/tasks" element={<TasksPage />} />
          <Route path="/timetable" element={<TimetablePage />} />
          <Route path="/goals" element={<GoalsPage />} />
          <Route path="/sessions" element={<SessionsPage />} />
          <Route path="/subjects" element={<SubjectsPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
