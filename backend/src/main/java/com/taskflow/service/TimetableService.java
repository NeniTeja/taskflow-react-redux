package com.taskflow.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taskflow.model.TimetableSlot;
import com.taskflow.repository.TimetableRepository;

@Service
public class TimetableService {
	private TimetableRepository repository;

	public TimetableService(TimetableRepository repository) {
		this.repository = repository;
	}

	public TimetableSlot addSlot(TimetableSlot slot) {
		if (slot.getSlotDate() == null) {
			slot.setSlotDate(LocalDate.now());
		}
		return repository.save(slot);
	}

	public List<TimetableSlot> getAllSlots() {
		return repository.findAll();
	}

	public List<TimetableSlot> getSlotsByDate(LocalDate date) {
		return repository.findBySlotDateOrderByStartTimeAsc(date);
	}

	public List<TimetableSlot> getTodaysSlots() {
		return repository.findBySlotDateOrderByStartTimeAsc(LocalDate.now());
	}

	public TimetableSlot updateSlot(int slotId, TimetableSlot updatedSlot) {
		TimetableSlot existing = repository.findById(slotId).orElse(null);
		if (existing == null) {
			return null;
		}
		existing.setSlotDate(updatedSlot.getSlotDate());
		existing.setStartTime(updatedSlot.getStartTime());
		existing.setEndTime(updatedSlot.getEndTime());
		existing.setActivityName(updatedSlot.getActivityName());
		return repository.save(existing);
	}

	public boolean deleteSlot(int slotId) {
		if (!repository.existsById(slotId)) {
			return false;
		}
		repository.deleteById(slotId);
		return true;
	}
}
