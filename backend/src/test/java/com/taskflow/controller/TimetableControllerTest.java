package com.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.model.TimetableSlot;
import com.taskflow.service.TimetableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimetableController.class)
public class TimetableControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TimetableService timetableService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void addSlot_ShouldReturnOk() throws Exception {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");

		when(timetableService.addSlot(any(TimetableSlot.class))).thenReturn(slot);

		mockMvc.perform(post("/api/timetable")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(slot)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activityName").value("Study"));
	}

	@Test
	void getSlots_WithoutDate_ShouldReturnAllSlots() throws Exception {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");
		List<TimetableSlot> slots = Arrays.asList(slot);

		when(timetableService.getAllSlots()).thenReturn(slots);

		mockMvc.perform(get("/api/timetable"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].activityName").value("Study"));
	}

	@Test
	void getSlots_WithDate_ShouldReturnSlotsForDate() throws Exception {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");
		List<TimetableSlot> slots = Arrays.asList(slot);
		LocalDate date = LocalDate.parse("2026-09-20");

		when(timetableService.getSlotsByDate(date)).thenReturn(slots);

		mockMvc.perform(get("/api/timetable").param("date", "2026-09-20"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].activityName").value("Study"));
	}

	@Test
	void getTodaysSlots_ShouldReturnOk() throws Exception {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");
		List<TimetableSlot> slots = Arrays.asList(slot);

		when(timetableService.getTodaysSlots()).thenReturn(slots);

		mockMvc.perform(get("/api/timetable/today"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].activityName").value("Study"));
	}

	@Test
	void updateSlot_WhenExists_ShouldReturnOk() throws Exception {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Updated Study");

		when(timetableService.updateSlot(eq(1), any(TimetableSlot.class))).thenReturn(slot);

		mockMvc.perform(put("/api/timetable/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(slot)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activityName").value("Updated Study"));
	}

	@Test
	void updateSlot_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		TimetableSlot slot = new TimetableSlot();

		when(timetableService.updateSlot(eq(1), any(TimetableSlot.class))).thenReturn(null);

		mockMvc.perform(put("/api/timetable/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(slot)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteSlot_WhenExists_ShouldReturnNoContent() throws Exception {
		when(timetableService.deleteSlot(1)).thenReturn(true);

		mockMvc.perform(delete("/api/timetable/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteSlot_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
		when(timetableService.deleteSlot(1)).thenReturn(false);

		mockMvc.perform(delete("/api/timetable/1"))
				.andExpect(status().isNotFound());
	}
}
