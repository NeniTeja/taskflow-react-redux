import React, { useEffect, useState } from "react";
import api from "../api/api";

function DashboardPage() {
  const [dashboard, setDashboard] = useState({ tasks: [], timetable: [], goals: [], sessions: [] });

  useEffect(() => {
    api.get("/dashboard").then((res) => setDashboard(res.data));
  }, []);

  return (
    <div className="page">
      <h2>Today's Dashboard</h2>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <h3>Pending Tasks</h3>
          {dashboard.tasks.length === 0 && <p>No pending tasks.</p>}
          <ul>
            {dashboard.tasks.map((task) => (
              <li key={task.taskId}>
                {task.title} <span className={`badge priority-${task.priority?.toLowerCase()}`}>{task.priority}</span>
              </li>
            ))}
          </ul>
        </div>

        <div className="dashboard-card">
          <h3>Today's Timetable</h3>
          {dashboard.timetable.length === 0 && <p>No slots planned for today.</p>}
          <ul>
            {dashboard.timetable.map((slot) => (
              <li key={slot.slotId}>
                {slot.startTime} - {slot.endTime}: {slot.activityName}
              </li>
            ))}
          </ul>
        </div>

        <div className="dashboard-card">
          <h3>Today's Goals</h3>
          {dashboard.goals.length === 0 && <p>No goals set for today.</p>}
          <ul>
            {dashboard.goals.map((goal) => (
              <li key={goal.goalId} className={goal.completed ? "completed-text" : ""}>
                {goal.title}
              </li>
            ))}
          </ul>
        </div>

        <div className="dashboard-card">
          <h3>Today's Sessions</h3>
          {dashboard.sessions.length === 0 && <p>No sessions logged today.</p>}
          <ul>
            {dashboard.sessions.map((session) => (
              <li key={session.sessionId}>
                {session.subject} - {session.durationMinutes} min
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
