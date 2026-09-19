import React, { useEffect, useState } from "react";
import api from "../api/api";

const today = () => new Date().toISOString().slice(0, 10);

const emptyForm = { title: "", goalDate: today(), completed: false };

function GoalsPage() {
  const [goals, setGoals] = useState([]);
  const [form, setForm] = useState(emptyForm);

  const loadGoals = () => {
    api.get("/goals").then((res) => setGoals(res.data));
  };

  useEffect(() => {
    loadGoals();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.title.trim()) return;

    api.post("/goals", form).then(() => {
      setForm({ ...emptyForm, goalDate: today() });
      loadGoals();
    });
  };

  const handleToggle = (goalId) => {
    api.put(`/goals/${goalId}/toggle`).then(() => loadGoals());
  };

  const handleDelete = (goalId) => {
    api.delete(`/goals/${goalId}`).then(() => loadGoals());
  };

  return (
    <div className="page">
      <h2>Daily Goals</h2>

      <form className="card-form" onSubmit={handleSubmit}>
        <input
          type="text"
          name="title"
          placeholder="Goal title"
          value={form.title}
          onChange={handleChange}
          required
        />
        <input type="date" name="goalDate" value={form.goalDate} onChange={handleChange} required />
        <button type="submit">Add Goal</button>
      </form>

      <div className="card-list">
        {goals.length === 0 && <p>No goals yet.</p>}
        {goals.map((goal) => (
          <div className="item-card" key={goal.goalId}>
            <label className="goal-checkbox">
              <input type="checkbox" checked={goal.completed} onChange={() => handleToggle(goal.goalId)} />
              <span className={goal.completed ? "completed-text" : ""}>{goal.title}</span>
            </label>
            <div className="item-card-footer">
              <span className="badge">{goal.goalDate}</span>
              <button className="danger" onClick={() => handleDelete(goal.goalId)}>
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default GoalsPage;
