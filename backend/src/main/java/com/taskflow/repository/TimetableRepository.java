package com.taskflow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskflow.model.TimetableSlot;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableSlot, Integer> {
	public List<TimetableSlot> findBySlotDateOrderByStartTimeAsc(LocalDate slotDate);
}
