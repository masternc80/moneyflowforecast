package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.Schedule;

import java.util.List;

public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
	Schedule findByBaseDayOfMonthAndIsPriorWeekend(int dayOfMonth, boolean isPriorWeekend);
}
