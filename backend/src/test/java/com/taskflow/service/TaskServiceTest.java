package com.taskflow.service;

import com.taskflow.model.Task;
import com.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskService taskService;

	@Test
	public void testAddTask_DefaultValues() {
		Task task = new Task();
		task.setTitle("Test Title");

		when(taskRepository.save(any(Task.class))).thenAnswer(i -> {
			Task saved = i.getArgument(0);
			saved.setTaskId(1);
			return saved;
		});

		Task savedTask = taskService.addTask(task);

		assertNotNull(savedTask);
		assertEquals("Pending", savedTask.getStatus());
		assertEquals("Medium", savedTask.getPriority());
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	public void testAddTask_CustomValues() {
		Task task = new Task();
		task.setTitle("Test Title");
		task.setStatus("Completed");
		task.setPriority("High");

		when(taskRepository.save(any(Task.class))).thenReturn(task);

		Task savedTask = taskService.addTask(task);

		assertNotNull(savedTask);
		assertEquals("Completed", savedTask.getStatus());
		assertEquals("High", savedTask.getPriority());
		verify(taskRepository, times(1)).save(task);
	}

	@Test
	public void testGetAllTasks() {
		Task t1 = new Task();
		Task t2 = new Task();
		when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

		List<Task> tasks = taskService.getAllTasks();

		assertEquals(2, tasks.size());
		verify(taskRepository, times(1)).findAll();
	}

	@Test
	public void testGetTaskById_Found() {
		Task t1 = new Task();
		t1.setTaskId(1);
		when(taskRepository.findById(1)).thenReturn(Optional.of(t1));

		Task result = taskService.getTaskById(1);

		assertNotNull(result);
		assertEquals(1, result.getTaskId());
	}

	@Test
	public void testGetTaskById_NotFound() {
		when(taskRepository.findById(1)).thenReturn(Optional.empty());

		Task result = taskService.getTaskById(1);

		assertNull(result);
	}

	@Test
	public void testGetTasksByFilter_BothPresent() {
		Task t1 = new Task();
		when(taskRepository.findByPriorityAndStatus("High", "Pending"))
				.thenReturn(Collections.singletonList(t1));

		List<Task> result = taskService.getTasksByFilter("High", "Pending");

		assertEquals(1, result.size());
		verify(taskRepository, times(1)).findByPriorityAndStatus("High", "Pending");
	}

	@Test
	public void testGetTasksByFilter_OnlyPriority() {
		Task t1 = new Task();
		when(taskRepository.findByPriority("High")).thenReturn(Collections.singletonList(t1));

		List<Task> result = taskService.getTasksByFilter("High", "");

		assertEquals(1, result.size());
		verify(taskRepository, times(1)).findByPriority("High");
	}

	@Test
	public void testGetTasksByFilter_OnlyStatus() {
		Task t1 = new Task();
		when(taskRepository.findByStatus("Pending")).thenReturn(Collections.singletonList(t1));

		List<Task> result = taskService.getTasksByFilter(null, "Pending");

		assertEquals(1, result.size());
		verify(taskRepository, times(1)).findByStatus("Pending");
	}

	@Test
	public void testGetTasksByFilter_Neither() {
		Task t1 = new Task();
		when(taskRepository.findAll()).thenReturn(Collections.singletonList(t1));

		List<Task> result = taskService.getTasksByFilter("  ", null);

		assertEquals(1, result.size());
		verify(taskRepository, times(1)).findAll();
	}

	@Test
	public void testGetPendingTasks() {
		Task t1 = new Task();
		when(taskRepository.findByStatusOrderByTaskIdDesc("Pending"))
				.thenReturn(Collections.singletonList(t1));

		List<Task> result = taskService.getPendingTasks();

		assertEquals(1, result.size());
		verify(taskRepository, times(1)).findByStatusOrderByTaskIdDesc("Pending");
	}

	@Test
	public void testUpdateTask_Found() {
		Task existing = new Task();
		existing.setTaskId(1);
		existing.setTitle("Old Title");
		
		Task updatedInfo = new Task();
		updatedInfo.setTitle("New Title");
		updatedInfo.setDescription("Desc");
		updatedInfo.setPriority("Low");
		updatedInfo.setStatus("Done");

		when(taskRepository.findById(1)).thenReturn(Optional.of(existing));
		when(taskRepository.save(existing)).thenReturn(existing);

		Task result = taskService.updateTask(1, updatedInfo);

		assertNotNull(result);
		assertEquals("New Title", result.getTitle());
		assertEquals("Desc", result.getDescription());
		assertEquals("Low", result.getPriority());
		assertEquals("Done", result.getStatus());
		verify(taskRepository, times(1)).save(existing);
	}

	@Test
	public void testUpdateTask_NotFound() {
		Task updatedInfo = new Task();
		when(taskRepository.findById(1)).thenReturn(Optional.empty());

		Task result = taskService.updateTask(1, updatedInfo);

		assertNull(result);
		verify(taskRepository, never()).save(any());
	}

	@Test
	public void testDeleteTask_Exists() {
		when(taskRepository.existsById(1)).thenReturn(true);

		boolean result = taskService.deleteTask(1);

		assertTrue(result);
		verify(taskRepository, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteTask_NotExists() {
		when(taskRepository.existsById(1)).thenReturn(false);

		boolean result = taskService.deleteTask(1);

		assertFalse(result);
		verify(taskRepository, never()).deleteById(any());
	}
}
