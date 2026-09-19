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
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.model.Goal;
import com.taskflow.service.GoalService;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
	private GoalService service;

	public GoalController(GoalService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Goal> addGoal(@RequestBody Goal goal) {
		Goal saved = service.addGoal(goal);
		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<Goal>> getGoals() {
		return ResponseEntity.ok(service.getAllGoals());
	}

	@GetMapping("/today")
	public ResponseEntity<List<Goal>> getTodaysGoals() {
		return ResponseEntity.ok(service.getTodaysGoals());
	}

	@PutMapping("/{goalId}")
	public ResponseEntity<Goal> updateGoal(@PathVariable int goalId, @RequestBody Goal goal) {
		Goal updated = service.updateGoal(goalId, goal);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@PutMapping("/{goalId}/toggle")
	public ResponseEntity<Goal> toggleGoal(@PathVariable int goalId) {
		Goal updated = service.toggleCompleted(goalId);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{goalId}")
	public ResponseEntity<Void> deleteGoal(@PathVariable int goalId) {
		boolean deleted = service.deleteGoal(goalId);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
