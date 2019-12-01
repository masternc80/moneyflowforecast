package com.mff.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mff.data.entities.Schedule;
import com.mff.repository.ScheduleRepository;

@Component
public class ScheduleFactory {

	@Autowired
	private HolidayKeeper holidayKeeper;
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@PostConstruct
	public void init() {
		
	}
	
	public Date generateDate(Schedule schedule, int month, int year) {
		if (!schedule.getIsPeriodic()) {
			GregorianCalendar date;
			do {
				int maxday = new GregorianCalendar(year, month, 1).getActualMaximum(Calendar.DATE);
				int day = schedule.getBaseDayOfMonth() > maxday ? maxday : schedule.getBaseDayOfMonth();
				date = new GregorianCalendar(year, month, day);
				int shiftDay = 0;
				boolean shift;
				do {
					shift = false;
					int dayOfWeek = date.get(Calendar.DAY_OF_WEEK) + shiftDay;
					if (dayOfWeek < 1) {
						dayOfWeek += 7;
					} else if (dayOfWeek > 7) {
						dayOfWeek -= 7;
					}
					if (dayOfWeek == Calendar.SUNDAY
							|| dayOfWeek == Calendar.SATURDAY
							|| holidayKeeper.isHoliday(day + shiftDay, month, year)) {
						if (schedule.getIsPriorWeekend()) {
							shiftDay--;
						} else {
							shiftDay++;
						}
						shift = true;
					}
				} while (shift);
				date.add(Calendar.DAY_OF_MONTH, shiftDay);
				month++;
				if (date.get(Calendar.MONTH) == Calendar.DECEMBER) {
					month = Calendar.JANUARY;
					year++;
				}
			} while (date.getTime().before(new Date()));
			return date.getTime();
		} else {
			Date now = new Date();
			GregorianCalendar date = new GregorianCalendar();
			date.setTime(schedule.getStartDate());
			while (date.getTime().before(now)) {
				date.add(convertUnit(schedule.getPeriodUnit()), schedule.getPeriodLength());
			}
			return date.getTime();
		}
	}

	private int convertUnit(Schedule.PERIOD_UNIT unit) {
		switch (unit) {
			case DAY:
				return Calendar.DAY_OF_MONTH;
			case WEEK:
				return Calendar.WEEK_OF_YEAR;
			case MONTH:
				return Calendar.MONTH;
		}
		return 0;
	}
	
	public Schedule createSchedule(int dayOfMonth, boolean priorWeekend) {
		Schedule s = scheduleRepository.findByBaseDayOfMonthAndIsPriorWeekend(dayOfMonth, priorWeekend);
		if (s == null) {
			return scheduleRepository.save(Schedule.builder()
					.baseDayOfMonth(dayOfMonth)
					.isPriorWeekend(priorWeekend)
					.build());
		}
		return s;
	}
	
	public Iterable<Schedule> getAll() {
		return scheduleRepository.findAll();
	}

}
