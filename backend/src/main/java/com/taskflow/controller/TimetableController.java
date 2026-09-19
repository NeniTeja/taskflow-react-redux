package com.taskflow.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.model.TimetableSlot;
import com.taskflow.service.TimetableService;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {
	private TimetableService service;

	public TimetableController(TimetableService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<TimetableSlot> addSlot(@RequestBody TimetableSlot slot) {
		TimetableSlot saved = service.addSlot(slot);
		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<TimetableSlot>> getSlots(
			@RequestParam(required = false) String date) {
		if (date != null && !date.trim().isEmpty()) {
			return ResponseEntity.ok(service.getSlotsByDate(LocalDate.parse(date)));
		}
		return ResponseEntity.ok(service.getAllSlots());
	}

	@GetMapping("/today")
	public ResponseEntity<List<TimetableSlot>> getTodaysSlots() {
		return ResponseEntity.ok(service.getTodaysSlots());
	}

	@PutMapping("/{slotId}")
	public ResponseEntity<TimetableSlot> updateSlot(@PathVariable int slotId, @RequestBody TimetableSlot slot) {
		TimetableSlot updated = service.updateSlot(slotId, slot);
		if (updated == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{slotId}")
	public ResponseEntity<Void> deleteSlot(@PathVariable int slotId) {
		boolean deleted = service.deleteSlot(slotId);
		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
