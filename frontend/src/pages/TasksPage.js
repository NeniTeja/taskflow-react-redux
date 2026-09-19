import React, { useEffect, useState } from "react";
import api from "../api/api";

const emptyForm = { title: "", description: "", priority: "Medium", status: "Pending" };

function TasksPage() {
  const [tasks, setTasks] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [priorityFilter, setPriorityFilter] = useState("");
  const [statusFilter, setStatusFilter] = useState("");

  const loadTasks = () => {
    const params = {};
    if (priorityFilter) params.priority = priorityFilter;
    if (statusFilter) params.status = statusFilter;

    api.get("/tasks", { params }).then((res) => setTasks(res.data));
  };

  useEffect(() => {
    loadTasks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [priorityFilter, statusFilter]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.title.trim()) return;

    if (editingId) {
      api.put(`/tasks/${editingId}`, form).then(() => {
        setForm(emptyForm);
        setEditingId(null);
        loadTasks();
      });
    } else {
      api.post("/tasks", form).then(() => {
        setForm(emptyForm);
        loadTasks();
      });
    }
  };

  const handleEdit = (task) => {
    setForm({
      title: task.title,
      description: task.description || "",
      priority: task.priority,
      status: task.status,
    });
    setEditingId(task.taskId);
  };

  const handleDelete = (taskId) => {
    api.delete(`/tasks/${taskId}`).then(() => loadTasks());
  };

  const handleCancelEdit = () => {
    setForm(emptyForm);
    setEditingId(null);
  };

  return (
    <div className="page">
      <h2>Tasks</h2>

      <form className="card-form" onSubmit={handleSubmit}>
        <input
          type="text"
          name="title"
          placeholder="Task title"
          value={form.title}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="description"
          placeholder="Description"
          value={form.description}
          onChange={handleChange}
        />
        <select name="priority" value={form.priority} onChange={handleChange}>
          <option value="Low">Low</option>
          <option value="Medium">Medium</option>
          <option value="High">High</option>
        </select>
        <select name="status" value={form.status} onChange={handleChange}>
          <option value="Pending">Pending</option>
          <option value="Completed">Completed</option>
        </select>
        <button type="submit">{editingId ? "Update Task" : "Add Task"}</button>
        {editingId && (
          <button type="button" className="secondary" onClick={handleCancelEdit}>
            Cancel
          </button>
        )}
      </form>

      <div className="filters">
        <label>
          Priority:
          <select value={priorityFilter} onChange={(e) => setPriorityFilter(e.target.value)}>
            <option value="">All</option>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
          </select>
        </label>
        <label>
          Status:
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="">All</option>
            <option value="Pending">Pending</option>
            <option value="Completed">Completed</option>
          </select>
        </label>
      </div>

      <div className="card-list">
        {tasks.length === 0 && <p>No tasks found.</p>}
        {tasks.map((task) => (
          <div className="item-card" key={task.taskId}>
            <div className="item-card-header">
              <strong>{task.title}</strong>
              <span className={`badge priority-${task.priority?.toLowerCase()}`}>{task.priority}</span>
            </div>
            {task.description && <p>{task.description}</p>}
            <div className="item-card-footer">
              <span className={`badge status-${task.status?.toLowerCase()}`}>{task.status}</span>
              <div className="item-actions">
                <button onClick={() => handleEdit(task)}>Edit</button>
                <button className="danger" onClick={() => handleDelete(task.taskId)}>
                  Delete
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TasksPage;
