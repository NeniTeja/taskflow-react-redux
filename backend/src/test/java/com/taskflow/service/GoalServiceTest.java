package com.taskflow.service;

import com.taskflow.model.Goal;
import com.taskflow.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {

	@Mock
	private GoalRepository goalRepository;

	@InjectMocks
	private GoalService goalService;

	@Test
	public void testAddGoal_NullDate() {
		Goal goal = new Goal();
		goal.setTitle("New Goal");

		when(goalRepository.save(any(Goal.class))).thenAnswer(i -> i.getArgument(0));

		Goal savedGoal = goalService.addGoal(goal);

		assertNotNull(savedGoal);
		assertEquals(LocalDate.now(), savedGoal.getGoalDate());
		verify(goalRepository, times(1)).save(goal);
	}

	@Test
	public void testAddGoal_WithDate() {
		Goal goal = new Goal();
		goal.setTitle("New Goal");
		LocalDate pastDate = LocalDate.of(2020, 1, 1);
		goal.setGoalDate(pastDate);

		when(goalRepository.save(any(Goal.class))).thenReturn(goal);

		Goal savedGoal = goalService.addGoal(goal);

		assertNotNull(savedGoal);
		assertEquals(pastDate, savedGoal.getGoalDate());
		verify(goalRepository, times(1)).save(goal);
	}

	@Test
	public void testGetAllGoals() {
		Goal g1 = new Goal();
		Goal g2 = new Goal();
		when(goalRepository.findAll()).thenReturn(Arrays.asList(g1, g2));

		List<Goal> goals = goalService.getAllGoals();

		assertEquals(2, goals.size());
		verify(goalRepository, times(1)).findAll();
	}

	@Test
	public void testGetTodaysGoals() {
		Goal g1 = new Goal();
		when(goalRepository.findByGoalDateOrderByGoalIdAsc(LocalDate.now()))
				.thenReturn(Collections.singletonList(g1));

		List<Goal> goals = goalService.getTodaysGoals();

		assertEquals(1, goals.size());
		verify(goalRepository, times(1)).findByGoalDateOrderByGoalIdAsc(LocalDate.now());
	}

	@Test
	public void testUpdateGoal_Found() {
		Goal existing = new Goal();
		existing.setGoalId(1);
		existing.setTitle("Old Title");
		
		Goal updatedInfo = new Goal();
		updatedInfo.setTitle("New Title");
		updatedInfo.setGoalDate(LocalDate.of(2025, 1, 1));
		updatedInfo.setCompleted(true);

		when(goalRepository.findById(1)).thenReturn(Optional.of(existing));
		when(goalRepository.save(existing)).thenReturn(existing);

		Goal result = goalService.updateGoal(1, updatedInfo);

		assertNotNull(result);
		assertEquals("New Title", result.getTitle());
		assertEquals(LocalDate.of(2025, 1, 1), result.getGoalDate());
		assertTrue(result.isCompleted());
		verify(goalRepository, times(1)).save(existing);
	}

	@Test
	public void testUpdateGoal_NotFound() {
		Goal updatedInfo = new Goal();
		when(goalRepository.findById(1)).thenReturn(Optional.empty());

		Goal result = goalService.updateGoal(1, updatedInfo);

		assertNull(result);
		verify(goalRepository, never()).save(any());
	}

	@Test
	public void testToggleCompleted_Found() {
		Goal existing = new Goal();
		existing.setGoalId(1);
		existing.setCompleted(false);

		when(goalRepository.findById(1)).thenReturn(Optional.of(existing));
		when(goalRepository.save(existing)).thenReturn(existing);

		Goal result = goalService.toggleCompleted(1);

		assertNotNull(result);
		assertTrue(result.isCompleted());
		verify(goalRepository, times(1)).save(existing);
	}

	@Test
	public void testToggleCompleted_NotFound() {
		when(goalRepository.findById(1)).thenReturn(Optional.empty());

		Goal result = goalService.toggleCompleted(1);

		assertNull(result);
		verify(goalRepository, never()).save(any());
	}

	@Test
	public void testDeleteGoal_Exists() {
		when(goalRepository.existsById(1)).thenReturn(true);

		boolean result = goalService.deleteGoal(1);

		assertTrue(result);
		verify(goalRepository, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteGoal_NotExists() {
		when(goalRepository.existsById(1)).thenReturn(false);

		boolean result = goalService.deleteGoal(1);

		assertFalse(result);
		verify(goalRepository, never()).deleteById(any());
	}
}
