package com.taskflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskflow.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
	public List<Goal> findByGoalDateOrderByGoalIdAsc(LocalDate goalDate);
}
