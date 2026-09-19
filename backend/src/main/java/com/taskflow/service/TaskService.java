package com.taskflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskflow.model.Task;
import com.taskflow.repository.TaskRepository;

@Service
public class TaskService {
	private TaskRepository repository;

	public TaskService(TaskRepository repository) {
		this.repository = repository;
	}

	public Task addTask(Task task) {
		if (isEmpty(task.getStatus())) {
			task.setStatus("Pending");
		}
		if (isEmpty(task.getPriority())) {
			task.setPriority("Medium");
		}
		return repository.save(task);
	}

	public List<Task> getAllTasks() {
		return repository.findAll();
	}

	public Task getTaskById(int taskId) {
		return repository.findById(taskId).orElse(null);
	}

	public List<Task> getTasksByFilter(String priority, String status) {
		boolean hasPriority = !isEmpty(priority);
		boolean hasStatus = !isEmpty(status);

		if (hasPriority && hasStatus) {
			return repository.findByPriorityAndStatus(priority, status);
		}
		if (hasPriority) {
			return repository.findByPriority(priority);
		}
		if (hasStatus) {
			return repository.findByStatus(status);
		}
		return repository.findAll();
	}

	public List<Task> getPendingTasks() {
		return repository.findByStatusOrderByTaskIdDesc("Pending");
	}

	public Task updateTask(int taskId, Task updatedTask) {
		Task existing = repository.findById(taskId).orElse(null);
		if (existing == null) {
			return null;
		}
		existing.setTitle(updatedTask.getTitle());
		existing.setDescription(updatedTask.getDescription());
		existing.setPriority(updatedTask.getPriority());
		existing.setStatus(updatedTask.getStatus());
		return repository.save(existing);
	}

	public boolean deleteTask(int taskId) {
		if (!repository.existsById(taskId)) {
			return false;
		}
		repository.deleteById(taskId);
		return true;
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}
}
