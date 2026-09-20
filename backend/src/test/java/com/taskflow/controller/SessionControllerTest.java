package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.model.StudySession;
import com.taskflow.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SessionService sessionService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void addSession_ShouldReturnOk() throws Exception {
		StudySession session = new StudySession();
		session.setSubject("Math");

		when(sessionService.addSession(any(StudySession.class))).thenReturn(session);

		mockMvc.perform(post("/api/sessions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(session)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.subject").value("Math"));
	}

	@Test
	void getSessions_ShouldReturnOk() throws Exception {
		StudySession session = new StudySession();
		session.setSubject("Math");
		List<StudySession> sessions = Arrays.asList(session);

		when(sessionService.getAllSessions()).thenReturn(sessions);

		mockMvc.perform(get("/api/sessions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].subject").value("Math"));
	}

	@Test
	void getTodaysSessions_ShouldReturnOk() throws Exception {
		StudySession session = new StudySession();
		session.setSubject("Math");
		List<StudySession> sessions = Arrays.asList(session);

		when(sessionService.getTodaysSessions()).thenReturn(sessions);

		mockMvc.perform(get("/api/sessions/today"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].subject").value("Math"));
	}

	@Test
	void updateSession_WhenExists_ShouldReturnOk() throws Exception {
		StudySession session = new StudySession();
		session.setSubject("Science");

		when(sessionService.updateSession(eq(1), any(StudySession.class))).thenReturn(session);

		mockMvc.perform(put("/api/sessions/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(session)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.subject").value("Science"));
	}

	@Test
	void updateSession_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		StudySession session = new StudySession();

		when(sessionService.updateSession(eq(1), any(StudySession.class))).thenReturn(null);

		mockMvc.perform(put("/api/sessions/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(session)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteSession_WhenExists_ShouldReturnNoContent() throws Exception {
		when(sessionService.deleteSession(1)).thenReturn(true);

		mockMvc.perform(delete("/api/sessions/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteSession_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(sessionService.deleteSession(1)).thenReturn(false);

		mockMvc.perform(delete("/api/sessions/1"))
				.andExpect(status().isNotFound());
	}
}
