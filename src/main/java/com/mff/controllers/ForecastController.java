package com.mff.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mff.data.dto.ForecastRecord;
import com.mff.data.entities.Account;
import com.mff.data.entities.RecurringTransaction;
import com.mff.data.entities.Schedule;
import com.mff.repository.AccountRepository;
import com.mff.repository.RecurringTransactionRepository;
import com.mff.util.ScheduleFactory;

import reactor.core.publisher.Flux;

import static java.util.Calendar.*;

@RestController
@RequestMapping("forecast")
public class ForecastController {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private RecurringTransactionRepository recurringTransactionRepository;
	
	@Autowired
	private ScheduleFactory scheduleFactory;
	
	@GetMapping
	public Flux<ForecastRecord> getForecast() {
		List<ForecastRecord> records = new ArrayList<>();
		double initialBalance = 0;
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar prevMonth = new GregorianCalendar();
		GregorianCalendar nextMonth = new GregorianCalendar();
		prevMonth.add(MONTH, -1);
		nextMonth.add(MONTH, 1);

		int days = now.getLeastMaximum(DATE);
		int prevdays = prevMonth.getActualMaximum(DATE);

		TreeMap<Integer, List<Schedule>> recordDays = new TreeMap<>();

		for (Schedule s : scheduleFactory.getAll()) {
			/*
			int day = scheduleFactory.generateDate(s, prevMonth.get(MONTH), prevMonth.get(YEAR));
			if (day > prevdays) {
				if (recordDays.containsKey(day - prevdays)) {
					recordDays.get(day - prevdays).add(s);
				} else {
					recordDays.put(day - prevdays, Arrays.asList(s));
				}
			}
			day = scheduleFactory.generateDate(s, now.get(MONTH), now.get(YEAR));
			if (day > 0) {
				if (recordDays.containsKey(day)) {
					recordDays.get(day).add(s);
				} else {
					recordDays.put(day, Arrays.asList(s));
				}
			}
			day = scheduleFactory.generateDate(s, nextMonth.get(MONTH), nextMonth.get(YEAR));
			if (day <= 0) {
				if (recordDays.containsKey(day + days)) {
					recordDays.get(day + days).add(s);
				} else {
					recordDays.put(day + days, Arrays.asList(s));
				}
			}
			*/
		}
		double curBalance = initialBalance;
		SimpleDateFormat dt = new SimpleDateFormat("dd-MMM"); 
		for (int i : recordDays.keySet()) {
			List<Schedule> todaySchedules = recordDays.get(i);
			for (Schedule s : todaySchedules) {
				LinkedList<RecurringTransaction> rs = new LinkedList<>();
				for (RecurringTransaction rt : recurringTransactionRepository.findAllByScheduleId(s.getId())) {
					if (rt.isCredit()) {
						curBalance += rt.getAmount();
						records.add(ForecastRecord.builder()
								.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
								.description(rt.getDescription())
								.credit(rt.getAmount())
								.debit(0)
								.balance(curBalance)
								.build());
					} else {
						rs.add(rt);
					}
				}
				for (RecurringTransaction rt : rs) {
					curBalance -= rt.getAmount();
					records.add(ForecastRecord.builder()
							.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.description(rt.getDescription())
							.credit(0)
							.debit(rt.getAmount())
							.balance(curBalance)
							.build());
				}
				for (Account a : accountRepository.findAllByScheduleId(s.getId())) {
					curBalance -= a.getStatementBalance();
					records.add(ForecastRecord.builder()
							.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.description(a.getName())
							.credit(0)
							.debit(a.getStatementBalance())
							.balance(curBalance)
							.build());
				}
			}
		}
		return Flux.fromIterable(records);
	}

}
