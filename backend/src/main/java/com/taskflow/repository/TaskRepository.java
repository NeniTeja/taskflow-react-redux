package com.taskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskflow.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
	public List<Task> findByPriority(String priority);

	public List<Task> findByStatus(String status);

	public List<Task> findByPriorityAndStatus(String priority, String status);

	public List<Task> findByStatusOrderByTaskIdDesc(String status);
}
