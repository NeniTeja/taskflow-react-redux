package com.taskflow.service;

import com.taskflow.model.StudySession;
import com.taskflow.repository.SessionRepository;
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
public class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@InjectMocks
	private SessionService sessionService;

	@Test
	public void testAddSession_NullDate() {
		StudySession session = new StudySession();
		session.setSubject("Math");

		when(sessionRepository.save(any(StudySession.class))).thenAnswer(i -> i.getArgument(0));

		StudySession savedSession = sessionService.addSession(session);

		assertNotNull(savedSession);
		assertEquals(LocalDate.now(), savedSession.getSessionDate());
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void testAddSession_WithDate() {
		StudySession session = new StudySession();
		session.setSubject("Math");
		LocalDate pastDate = LocalDate.of(2020, 1, 1);
		session.setSessionDate(pastDate);

		when(sessionRepository.save(any(StudySession.class))).thenReturn(session);

		StudySession savedSession = sessionService.addSession(session);

		assertNotNull(savedSession);
		assertEquals(pastDate, savedSession.getSessionDate());
		verify(sessionRepository, times(1)).save(session);
	}

	@Test
	public void testGetAllSessions() {
		StudySession s1 = new StudySession();
		StudySession s2 = new StudySession();
		when(sessionRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

		List<StudySession> sessions = sessionService.getAllSessions();

		assertEquals(2, sessions.size());
		verify(sessionRepository, times(1)).findAll();
	}

	@Test
	public void testGetTodaysSessions() {
		StudySession s1 = new StudySession();
		when(sessionRepository.findBySessionDateOrderBySessionIdDesc(LocalDate.now()))
				.thenReturn(Collections.singletonList(s1));

		List<StudySession> sessions = sessionService.getTodaysSessions();

		assertEquals(1, sessions.size());
		verify(sessionRepository, times(1)).findBySessionDateOrderBySessionIdDesc(LocalDate.now());
	}

	@Test
	public void testUpdateSession_Found() {
		StudySession existing = new StudySession();
		existing.setSessionId(1);
		existing.setSubject("Old Subject");
		
		StudySession updatedInfo = new StudySession();
		updatedInfo.setSubject("New Subject");
		updatedInfo.setDurationMinutes(60);
		updatedInfo.setSessionDate(LocalDate.of(2025, 1, 1));

		when(sessionRepository.findById(1)).thenReturn(Optional.of(existing));
		when(sessionRepository.save(existing)).thenReturn(existing);

		StudySession result = sessionService.updateSession(1, updatedInfo);

		assertNotNull(result);
		assertEquals("New Subject", result.getSubject());
		assertEquals(60, result.getDurationMinutes());
		assertEquals(LocalDate.of(2025, 1, 1), result.getSessionDate());
		verify(sessionRepository, times(1)).save(existing);
	}

	@Test
	public void testUpdateSession_NotFound() {
		StudySession updatedInfo = new StudySession();
		when(sessionRepository.findById(1)).thenReturn(Optional.empty());

		StudySession result = sessionService.updateSession(1, updatedInfo);

		assertNull(result);
		verify(sessionRepository, never()).save(any());
	}

	@Test
	public void testDeleteSession_Exists() {
		when(sessionRepository.existsById(1)).thenReturn(true);

		boolean result = sessionService.deleteSession(1);

		assertTrue(result);
		verify(sessionRepository, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteSession_NotExists() {
		when(sessionRepository.existsById(1)).thenReturn(false);

		boolean result = sessionService.deleteSession(1);

		assertFalse(result);
		verify(sessionRepository, never()).deleteById(any());
	}
}
