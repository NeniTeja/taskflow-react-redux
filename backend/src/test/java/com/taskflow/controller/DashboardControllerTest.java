package com.taskflow.controller;

import com.taskflow.model.Goal;
import com.taskflow.model.StudySession;
import com.taskflow.model.Task;
import com.taskflow.model.TimetableSlot;
import com.taskflow.service.GoalService;
import com.taskflow.service.SessionService;
import com.taskflow.service.TaskService;
import com.taskflow.service.TimetableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private TimetableService timetableService;

	@MockBean
	private GoalService goalService;

	@MockBean
	private SessionService sessionService;

	@Test
	void getDashboard_ShouldReturnDashboardData() throws Exception {
		Task task = new Task();
		task.setTitle("Pending Task");
		
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Today Slot");
		
		Goal goal = new Goal();
		goal.setTitle("Today Goal");
		
		StudySession session = new StudySession();
		session.setSubject("Today Session");

		when(taskService.getPendingTasks()).thenReturn(Arrays.asList(task));
		when(timetableService.getTodaysSlots()).thenReturn(Arrays.asList(slot));
		when(goalService.getTodaysGoals()).thenReturn(Arrays.asList(goal));
		when(sessionService.getTodaysSessions()).thenReturn(Arrays.asList(session));

		mockMvc.perform(get("/api/dashboard"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tasks[0].title").value("Pending Task"))
				.andExpect(jsonPath("$.timetable[0].activityName").value("Today Slot"))
				.andExpect(jsonPath("$.goals[0].title").value("Today Goal"))
				.andExpect(jsonPath("$.sessions[0].subject").value("Today Session"));
	}
}
