package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.model.Task;
import com.taskflow.service.TaskService;
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

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void addTask_ShouldReturnOk() throws Exception {
		Task task = new Task();
		task.setTitle("Test Task");

		when(taskService.addTask(any(Task.class))).thenReturn(task);

		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Test Task"));
	}

	@Test
	void getTasks_WithFilters_ShouldReturnOk() throws Exception {
		Task task = new Task();
		task.setTitle("Test Task");
		List<Task> tasks = Arrays.asList(task);

		when(taskService.getTasksByFilter("High", "Pending")).thenReturn(tasks);

		mockMvc.perform(get("/api/tasks")
				.param("priority", "High")
				.param("status", "Pending"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Test Task"));
	}

	@Test
	void getTasks_WithoutFilters_ShouldReturnOk() throws Exception {
		Task task = new Task();
		task.setTitle("All Task");
		List<Task> tasks = Arrays.asList(task);

		when(taskService.getTasksByFilter(null, null)).thenReturn(tasks);

		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("All Task"));
	}

	@Test
	void getTask_WhenExists_ShouldReturnOk() throws Exception {
		Task task = new Task();
		task.setTaskId(1);
		task.setTitle("Test Task");

		when(taskService.getTaskById(1)).thenReturn(task);

		mockMvc.perform(get("/api/tasks/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Test Task"));
	}

	@Test
	void getTask_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(taskService.getTaskById(1)).thenReturn(null);

		mockMvc.perform(get("/api/tasks/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateTask_WhenExists_ShouldReturnOk() throws Exception {
		Task task = new Task();
		task.setTitle("Updated Task");

		when(taskService.updateTask(eq(1), any(Task.class))).thenReturn(task);

		mockMvc.perform(put("/api/tasks/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Task"));
	}

	@Test
	void updateTask_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		Task task = new Task();
		task.setTitle("Updated Task");

		when(taskService.updateTask(eq(1), any(Task.class))).thenReturn(null);

		mockMvc.perform(put("/api/tasks/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteTask_WhenExists_ShouldReturnNoContent() throws Exception {
		when(taskService.deleteTask(1)).thenReturn(true);

		mockMvc.perform(delete("/api/tasks/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteTask_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(taskService.deleteTask(1)).thenReturn(false);

		mockMvc.perform(delete("/api/tasks/1"))
				.andExpect(status().isNotFound());
	}
}
