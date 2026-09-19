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

import com.taskflow.model.StudySession;
import com.taskflow.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
	private SessionService service;

	public SessionController(SessionService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<StudySession> addSession(@RequestBody StudySession session) {
		StudySession saved = service.addSession(session);
		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<StudySession>> getSessions() {
		return ResponseEntity.ok(service.getAllSessions());
	}

	@GetMapping("/today")
	public ResponseEntity<List<StudySession>> getTodaysSessions() {
		return ResponseEntity.ok(service.getTodaysSessions());
	}

	@PutMapping("/{sessionId}")
	public ResponseEntity<StudySession> updateSession(@PathVariable int sessionId, @RequestBody StudySession session) {
		StudySession updated = service.updateSession(sessionId, session);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{sessionId}")
	public ResponseEntity<Void> deleteSession(@PathVariable int sessionId) {
		boolean deleted = service.deleteSession(sessionId);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
