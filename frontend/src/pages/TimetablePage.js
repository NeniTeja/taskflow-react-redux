import React, { useEffect, useState } from "react";
import api from "../api/api";

const today = () => new Date().toISOString().slice(0, 10);

const emptyForm = { slotDate: today(), startTime: "", endTime: "", activityName: "" };

function TimetablePage() {
  const [slots, setSlots] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  const loadSlots = () => {
    api.get("/timetable", { params: { date: form.slotDate } }).then((res) => setSlots(res.data));
  };

  useEffect(() => {
    loadSlots();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [form.slotDate]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.activityName.trim() || !form.startTime || !form.endTime) return;

    if (editingId) {
      api.put(`/timetable/${editingId}`, form).then(() => {
        setEditingId(null);
        setForm({ ...emptyForm, slotDate: form.slotDate });
        loadSlots();
      });
    } else {
      api.post("/timetable", form).then(() => {
        setForm({ ...emptyForm, slotDate: form.slotDate });
        loadSlots();
      });
    }
  };

  const handleEdit = (slot) => {
    setForm({
      slotDate: slot.slotDate,
      startTime: slot.startTime,
      endTime: slot.endTime,
      activityName: slot.activityName,
    });
    setEditingId(slot.slotId);
  };

  const handleDelete = (slotId) => {
    api.delete(`/timetable/${slotId}`).then(() => loadSlots());
  };

  return (
    <div className="page">
      <h2>Daily Timetable</h2>

      <form className="card-form" onSubmit={handleSubmit}>
        <input type="date" name="slotDate" value={form.slotDate} onChange={handleChange} required />
        <input type="time" name="startTime" value={form.startTime} onChange={handleChange} required />
        <input type="time" name="endTime" value={form.endTime} onChange={handleChange} required />
        <input
          type="text"
          name="activityName"
          placeholder="Activity name"
          value={form.activityName}
          onChange={handleChange}
          required
        />
        <button type="submit">{editingId ? "Update Slot" : "Add Slot"}</button>
        {editingId && (
          <button
            type="button"
            className="secondary"
            onClick={() => {
              setEditingId(null);
              setForm({ ...emptyForm, slotDate: form.slotDate });
            }}
          >
            Cancel
          </button>
        )}
      </form>

      <div className="card-list">
        {slots.length === 0 && <p>No timetable slots for this date.</p>}
        {slots.map((slot) => (
          <div className="item-card" key={slot.slotId}>
            <div className="item-card-header">
              <strong>{slot.activityName}</strong>
              <span className="badge">
                {slot.startTime} - {slot.endTime}
              </span>
            </div>
            <div className="item-actions">
              <button onClick={() => handleEdit(slot)}>Edit</button>
              <button className="danger" onClick={() => handleDelete(slot.slotId)}>
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TimetablePage;
