package com.mff.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mff.data.entities.Holiday;
import com.mff.util.HolidayKeeper;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("holidays")
public class HolidayController {

	@Autowired
	private HolidayKeeper holidayKeeper;
	
	@GetMapping("/{year}")
	public Flux<Holiday> getHolidays(@PathVariable int year) {
		return Flux.fromIterable(holidayKeeper.getHolidays(year));
	}
}
