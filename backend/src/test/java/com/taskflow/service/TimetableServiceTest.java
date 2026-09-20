package com.taskflow.service;

import com.taskflow.model.TimetableSlot;
import com.taskflow.repository.TimetableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimetableServiceTest {

	@Mock
	private TimetableRepository timetableRepository;

	@InjectMocks
	private TimetableService timetableService;

	@Test
	public void testAddSlot_NullDate() {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");

		when(timetableRepository.save(any(TimetableSlot.class))).thenAnswer(i -> i.getArgument(0));

		TimetableSlot savedSlot = timetableService.addSlot(slot);

		assertNotNull(savedSlot);
		assertEquals(LocalDate.now(), savedSlot.getSlotDate());
		verify(timetableRepository, times(1)).save(slot);
	}

	@Test
	public void testAddSlot_WithDate() {
		TimetableSlot slot = new TimetableSlot();
		slot.setActivityName("Study");
		LocalDate pastDate = LocalDate.of(2020, 1, 1);
		slot.setSlotDate(pastDate);

		when(timetableRepository.save(any(TimetableSlot.class))).thenReturn(slot);

		TimetableSlot savedSlot = timetableService.addSlot(slot);

		assertNotNull(savedSlot);
		assertEquals(pastDate, savedSlot.getSlotDate());
		verify(timetableRepository, times(1)).save(slot);
	}

	@Test
	public void testGetAllSlots() {
		TimetableSlot s1 = new TimetableSlot();
		TimetableSlot s2 = new TimetableSlot();
		when(timetableRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

		List<TimetableSlot> slots = timetableService.getAllSlots();

		assertEquals(2, slots.size());
		verify(timetableRepository, times(1)).findAll();
	}

	@Test
	public void testGetSlotsByDate() {
		TimetableSlot s1 = new TimetableSlot();
		LocalDate date = LocalDate.of(2025, 1, 1);
		when(timetableRepository.findBySlotDateOrderByStartTimeAsc(date))
				.thenReturn(Collections.singletonList(s1));

		List<TimetableSlot> slots = timetableService.getSlotsByDate(date);

		assertEquals(1, slots.size());
		verify(timetableRepository, times(1)).findBySlotDateOrderByStartTimeAsc(date);
	}

	@Test
	public void testGetTodaysSlots() {
		TimetableSlot s1 = new TimetableSlot();
		when(timetableRepository.findBySlotDateOrderByStartTimeAsc(LocalDate.now()))
				.thenReturn(Collections.singletonList(s1));

		List<TimetableSlot> slots = timetableService.getTodaysSlots();

		assertEquals(1, slots.size());
		verify(timetableRepository, times(1)).findBySlotDateOrderByStartTimeAsc(LocalDate.now());
	}

	@Test
	public void testUpdateSlot_Found() {
		TimetableSlot existing = new TimetableSlot();
		existing.setSlotId(1);
		existing.setActivityName("Old Activity");
		
		TimetableSlot updatedInfo = new TimetableSlot();
		updatedInfo.setActivityName("New Activity");
		updatedInfo.setSlotDate(LocalDate.of(2025, 1, 1));
		updatedInfo.setStartTime(LocalTime.of(10, 0));
		updatedInfo.setEndTime(LocalTime.of(11, 0));

		when(timetableRepository.findById(1)).thenReturn(Optional.of(existing));
		when(timetableRepository.save(existing)).thenReturn(existing);

		TimetableSlot result = timetableService.updateSlot(1, updatedInfo);

		assertNotNull(result);
		assertEquals("New Activity", result.getActivityName());
		assertEquals(LocalDate.of(2025, 1, 1), result.getSlotDate());
		assertEquals(LocalTime.of(10, 0), result.getStartTime());
		assertEquals(LocalTime.of(11, 0), result.getEndTime());
		verify(timetableRepository, times(1)).save(existing);
	}

	@Test
	public void testUpdateSlot_NotFound() {
		TimetableSlot updatedInfo = new TimetableSlot();
		when(timetableRepository.findById(1)).thenReturn(Optional.empty());

		TimetableSlot result = timetableService.updateSlot(1, updatedInfo);

		assertNull(result);
		verify(timetableRepository, never()).save(any());
	}

	@Test
	public void testDeleteSlot_Exists() {
		when(timetableRepository.existsById(1)).thenReturn(true);

		boolean result = timetableService.deleteSlot(1);

		assertTrue(result);
		verify(timetableRepository, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteSlot_NotExists() {
		when(timetableRepository.existsById(1)).thenReturn(false);

		boolean result = timetableService.deleteSlot(1);

		assertFalse(result);
		verify(timetableRepository, never()).deleteById(any());
	}
}
