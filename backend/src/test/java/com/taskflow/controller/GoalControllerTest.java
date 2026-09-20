package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.model.Goal;
import com.taskflow.service.GoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
public class GoalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GoalService goalService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void addGoal_ShouldReturnOk() throws Exception {
		Goal goal = new Goal();
		goal.setTitle("Test Goal");

		when(goalService.addGoal(any(Goal.class))).thenReturn(goal);

		mockMvc.perform(post("/api/goals")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(goal)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Test Goal"));
	}

	@Test
	void getGoals_ShouldReturnOk() throws Exception {
		Goal goal = new Goal();
		goal.setTitle("Test Goal");
		List<Goal> goals = Arrays.asList(goal);

		when(goalService.getAllGoals()).thenReturn(goals);

		mockMvc.perform(get("/api/goals"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Test Goal"));
	}

	@Test
	void getTodaysGoals_ShouldReturnOk() throws Exception {
		Goal goal = new Goal();
		goal.setTitle("Today Goal");
		List<Goal> goals = Arrays.asList(goal);

		when(goalService.getTodaysGoals()).thenReturn(goals);

		mockMvc.perform(get("/api/goals/today"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Today Goal"));
	}

	@Test
	void updateGoal_WhenExists_ShouldReturnOk() throws Exception {
		Goal goal = new Goal();
		goal.setTitle("Updated Goal");

		when(goalService.updateGoal(eq(1), any(Goal.class))).thenReturn(goal);

		mockMvc.perform(put("/api/goals/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(goal)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Goal"));
	}

	@Test
	void updateGoal_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		Goal goal = new Goal();

		when(goalService.updateGoal(eq(1), any(Goal.class))).thenReturn(null);

		mockMvc.perform(put("/api/goals/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(goal)))
				.andExpect(status().isNotFound());
	}

	@Test
	void toggleGoal_WhenExists_ShouldReturnOk() throws Exception {
		Goal goal = new Goal();
		goal.setCompleted(true);

		when(goalService.toggleCompleted(1)).thenReturn(goal);

		mockMvc.perform(put("/api/goals/1/toggle"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.completed").value(true));
	}

	@Test
	void toggleGoal_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(goalService.toggleCompleted(1)).thenReturn(null);

		mockMvc.perform(put("/api/goals/1/toggle"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteGoal_WhenExists_ShouldReturnNoContent() throws Exception {
		when(goalService.deleteGoal(1)).thenReturn(true);

		mockMvc.perform(delete("/api/goals/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteGoal_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(goalService.deleteGoal(1)).thenReturn(false);

		mockMvc.perform(delete("/api/goals/1"))
				.andExpect(status().isNotFound());
	}
}
