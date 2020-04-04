package com.mff.controllers;

import java.text.SimpleDateFormat;
import java.util.*;

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
			Date day = scheduleFactory.generateDate(s, now.get(MONTH), now.get(YEAR));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(day);
			if (recordDays.containsKey(cal.get(DAY_OF_MONTH))) {
				recordDays.get(cal.get(DAY_OF_MONTH)).add(s);
			} else {
				recordDays.put(cal.get(DAY_OF_MONTH), Arrays.asList(s));
			}
		}
		double curBalance = initialBalance;
		SimpleDateFormat dt = new SimpleDateFormat("dd-MMM");
		SimpleDateFormat dtv = new SimpleDateFormat("YYYY-MM-dd");
		long id = 1;
		for (int i : recordDays.keySet()) {
			List<Schedule> todaySchedules = recordDays.get(i);
			for (Schedule s : todaySchedules) {
				LinkedList<RecurringTransaction> rs = new LinkedList<>();
				for (RecurringTransaction rt : recurringTransactionRepository.findAllBySchedulesIdIs(s.getId())) {
					if (rt.getAmount() > 0) {
						curBalance += rt.getAmount();
						records.add(ForecastRecord.builder()
								.id(id++)
								.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
								.recordDateValue(dtv.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
								.description(rt.getName())
								.credit(rt.getAmount())
								.debit(0)
								.balance(curBalance)
								.past(false)
								.build());
					} else {
						rs.add(rt);
					}
				}
				for (RecurringTransaction rt : rs) {
					curBalance += rt.getAmount();
					records.add(ForecastRecord.builder()
							.id(id++)
							.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.recordDateValue(dtv.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.description(rt.getName())
							.credit(0)
							.debit(-rt.getAmount())
							.balance(curBalance)
							.past(false)
							.build());
				}
				for (Account a : accountRepository.findAllByScheduleId(s.getId())) {
					curBalance -= a.getStatementBalance();
					records.add(ForecastRecord.builder()
							.id(id++)
							.recordDate(dt.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.recordDateValue(dtv.format(new GregorianCalendar(now.get(YEAR), now.get(MONTH), i).getTime()))
							.description(a.getName())
							.credit(0)
							.debit(a.getStatementBalance())
							.balance(curBalance)
							.past(false)
							.build());
				}
			}
		}
		return Flux.fromIterable(records);
	}

}
