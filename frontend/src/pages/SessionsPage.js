import React, { useEffect, useState } from "react";
import api from "../api/api";

const today = () => new Date().toISOString().slice(0, 10);

const emptyForm = { subject: "", durationMinutes: "", sessionDate: today() };

function SessionsPage() {
  const [sessions, setSessions] = useState([]);
  const [form, setForm] = useState(emptyForm);

  const loadSessions = () => {
    api.get("/sessions").then((res) => setSessions(res.data));
  };

  useEffect(() => {
    loadSessions();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.subject.trim() || !form.durationMinutes) return;

    api.post("/sessions", { ...form, durationMinutes: Number(form.durationMinutes) }).then(() => {
      setForm({ ...emptyForm, sessionDate: today() });
      loadSessions();
    });
  };

  const handleDelete = (sessionId) => {
    api.delete(`/sessions/${sessionId}`).then(() => loadSessions());
  };

  return (
    <div className="page">
      <h2>Study / Work Sessions</h2>

      <form className="card-form" onSubmit={handleSubmit}>
        <input
          type="text"
          name="subject"
          placeholder="Subject"
          value={form.subject}
          onChange={handleChange}
          required
        />
        <input
          type="number"
          name="durationMinutes"
          placeholder="Duration (minutes)"
          value={form.durationMinutes}
          onChange={handleChange}
          min="1"
          required
        />
        <input type="date" name="sessionDate" value={form.sessionDate} onChange={handleChange} required />
        <button type="submit">Add Session</button>
      </form>

      <div className="card-list">
        {sessions.length === 0 && <p>No sessions logged yet.</p>}
        {sessions.map((session) => (
          <div className="item-card" key={session.sessionId}>
            <div className="item-card-header">
              <strong>{session.subject}</strong>
              <span className="badge">{session.durationMinutes} min</span>
            </div>
            <div className="item-card-footer">
              <span className="badge">{session.sessionDate}</span>
              <button className="danger" onClick={() => handleDelete(session.sessionId)}>
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default SessionsPage;
