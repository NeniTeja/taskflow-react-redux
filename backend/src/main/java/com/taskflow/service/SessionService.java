package com.taskflow.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taskflow.model.StudySession;
import com.taskflow.repository.SessionRepository;

@Service
public class SessionService {
	private SessionRepository repository;

	public SessionService(SessionRepository repository) {
		this.repository = repository;
	}

	public StudySession addSession(StudySession session) {
		if (session.getSessionDate() == null) {
			session.setSessionDate(LocalDate.now());
		}
		return repository.save(session);
	}

	public List<StudySession> getAllSessions() {
		return repository.findAll();
	}

	public List<StudySession> getTodaysSessions() {
		return repository.findBySessionDateOrderBySessionIdDesc(LocalDate.now());
	}

	public StudySession updateSession(int sessionId, StudySession updatedSession) {
		StudySession existing = repository.findById(sessionId).orElse(null);
		if (existing == null) {
			return null;
		}
		existing.setSubject(updatedSession.getSubject());
		existing.setDurationMinutes(updatedSession.getDurationMinutes());
		existing.setSessionDate(updatedSession.getSessionDate());
		return repository.save(existing);
	}

	public boolean deleteSession(int sessionId) {
		if (!repository.existsById(sessionId)) {
			return false;
		}
		repository.deleteById(sessionId);
		return true;
	}
}
