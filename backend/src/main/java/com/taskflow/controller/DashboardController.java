package com.taskflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.model.Goal;
import com.taskflow.model.StudySession;
import com.taskflow.model.Task;
import com.taskflow.model.TimetableSlot;
import com.taskflow.service.GoalService;
import com.taskflow.service.SessionService;
import com.taskflow.service.TaskService;
import com.taskflow.service.TimetableService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	private TaskService taskService;
	private TimetableService timetableService;
	private GoalService goalService;
	private SessionService sessionService;

	public DashboardController(TaskService taskService, TimetableService timetableService,
			GoalService goalService, SessionService sessionService) {
		this.taskService = taskService;
		this.timetableService = timetableService;
		this.goalService = goalService;
		this.sessionService = sessionService;
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getDashboard() {
		List<Task> pendingTasks = taskService.getPendingTasks();
		List<TimetableSlot> todaysSlots = timetableService.getTodaysSlots();
		List<Goal> todaysGoals = goalService.getTodaysGoals();
		List<StudySession> todaysSessions = sessionService.getTodaysSessions();

		Map<String, Object> dashboard = new HashMap<String, Object>();
		dashboard.put("tasks", pendingTasks);
		dashboard.put("timetable", todaysSlots);
		dashboard.put("goals", todaysGoals);
		dashboard.put("sessions", todaysSessions);
		return ResponseEntity.ok(dashboard);
	}
}
