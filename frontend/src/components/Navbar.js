import React from "react";
import { NavLink } from "react-router-dom";

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-brand">TaskFlow</div>
      <div className="navbar-links">
        <NavLink to="/" end>Dashboard</NavLink>
        <NavLink to="/tasks">Tasks</NavLink>
        <NavLink to="/timetable">Timetable</NavLink>
        <NavLink to="/goals">Goals</NavLink>
        <NavLink to="/sessions">Sessions</NavLink>
        <NavLink to="/subjects">Subjects</NavLink>
      </div>
    </nav>
  );
}

export default Navbar;
