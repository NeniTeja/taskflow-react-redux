package com.taskflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskflow.model.StudySession;

@Repository
public interface SessionRepository extends JpaRepository<StudySession, Integer> {
	public List<StudySession> findBySessionDateOrderBySessionIdDesc(LocalDate sessionDate);
}
