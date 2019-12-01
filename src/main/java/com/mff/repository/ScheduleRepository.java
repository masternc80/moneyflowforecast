package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
	public Schedule findByBaseDayOfMonthAndIsPriorWeekend(int dayOfMonth, boolean isPriorWeekend);

}
