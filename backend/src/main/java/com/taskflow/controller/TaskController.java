package com.taskflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.model.Task;
import com.taskflow.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	private TaskService service;

	public TaskController(TaskService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Task> addTask(@RequestBody Task task) {
		Task saved = service.addTask(task);
		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<Task>> getTasks(
			@RequestParam(required = false) String priority,
			@RequestParam(required = false) String status) {
		List<Task> tasks = service.getTasksByFilter(priority, status);
		return ResponseEntity.ok(tasks);
	}

	@GetMapping("/{taskId}")
	public ResponseEntity<Task> getTask(@PathVariable int taskId) {
		Task task = service.getTaskById(taskId);
		if (task == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(task);
	}

	@PutMapping("/{taskId}")
	public ResponseEntity<Task> updateTask(@PathVariable int taskId, @RequestBody Task task) {
		Task updated = service.updateTask(taskId, task);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{taskId}")
	public ResponseEntity<Void> deleteTask(@PathVariable int taskId) {
		boolean deleted = service.deleteTask(taskId);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
