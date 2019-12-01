package com.mff.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mff.data.dto.RecurringTransactionDto;
import com.mff.data.entities.RecurringTransaction;
import com.mff.repository.RecurringTransactionRepository;
import com.mff.util.ScheduleFactory;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("recurring")
public class RecurringTransactionController {
	
	@Autowired
	private RecurringTransactionRepository repository;
	
	@Autowired
	private ScheduleFactory scheduleFactory;
	@PostMapping
	public Mono<RecurringTransaction> create(@RequestBody RecurringTransactionDto dto) {
		return Mono.just(repository.save(RecurringTransaction.builder()
				.credit(dto.isCredit())
				.amount(dto.getAmount())
				.description(dto.getDescription())
				.schedule(scheduleFactory.createSchedule(dto.getDayOfMonth(), dto.isPriorWeekend()))
				.build()));
	}
}
