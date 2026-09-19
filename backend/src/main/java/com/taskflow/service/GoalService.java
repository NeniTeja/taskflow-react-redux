package com.taskflow.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taskflow.model.Goal;
import com.taskflow.repository.GoalRepository;

@Service
public class GoalService {
	private GoalRepository repository;

	public GoalService(GoalRepository repository) {
		this.repository = repository;
	}

	public Goal addGoal(Goal goal) {
		if (goal.getGoalDate() == null) {
			goal.setGoalDate(LocalDate.now());
		}
		return repository.save(goal);
	}

	public List<Goal> getAllGoals() {
		return repository.findAll();
	}

	public List<Goal> getTodaysGoals() {
		return repository.findByGoalDateOrderByGoalIdAsc(LocalDate.now());
	}

	public Goal updateGoal(int goalId, Goal updatedGoal) {
		Goal existing = repository.findById(goalId).orElse(null);
		if (existing == null) {
			return null;
		}
		existing.setTitle(updatedGoal.getTitle());
		existing.setGoalDate(updatedGoal.getGoalDate());
		existing.setCompleted(updatedGoal.isCompleted());
		return repository.save(existing);
	}

	public Goal toggleCompleted(int goalId) {
		Goal existing = repository.findById(goalId).orElse(null);
		if (existing == null) {
			return null;
		}
		existing.setCompleted(!existing.isCompleted());
		return repository.save(existing);
	}

	public boolean deleteGoal(int goalId) {
		if (!repository.existsById(goalId)) {
			return false;
		}
		repository.deleteById(goalId);
		return true;
	}
}
