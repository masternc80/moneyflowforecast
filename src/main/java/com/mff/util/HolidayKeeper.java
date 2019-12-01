package com.mff.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mff.data.entities.Holiday;
import com.mff.repository.HolidayRepository;

@Component
public class HolidayKeeper {
	
	@Autowired
	private HolidayRepository holidayRepository;

	private Map<Integer, List<Holiday>> holidays = new HashMap<>();
	
	@PostConstruct
	public void init() {
		holidayRepository.findAll().forEach(holiday -> {
			if (holidays.containsKey(holiday.getYear())) {
				holidays.get(holiday.getYear()).add(holiday);
			} else {
				holidays.put(holiday.getYear(), new ArrayList<Holiday>(Arrays.asList(holiday)));
			}
		});
	}
	
	public boolean isHoliday(int day, int month, int year) {
		if (!holidays.containsKey(year)) {
			return false;
		}
		Date d = new GregorianCalendar(year, month, day).getTime();
		for (Holiday h : holidays.get(year)) {
			if (h.getDate().equals(d)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Holiday> getHolidays(int year) {
		if (holidays.get(year) == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(holidays.get(year));
	}
}
