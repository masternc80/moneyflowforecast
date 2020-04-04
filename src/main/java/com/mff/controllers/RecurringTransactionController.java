package com.mff.controllers;

import com.mff.data.dto.NewRecurringTransactionDto;
import com.mff.data.dto.RecurringTransactionDto;
import com.mff.data.entities.RecurringTransaction;
import com.mff.data.entities.Schedule;
import com.mff.repository.RecurringTransactionRepository;
import com.mff.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("recurring")
public class RecurringTransactionController extends AbstractController<RecurringTransaction, RecurringTransactionDto, NewRecurringTransactionDto, RecurringTransactionRepository> {
	
	@Autowired
	private ScheduleRepository scheduleRepository;

	RecurringTransactionController(RecurringTransactionRepository repository) {
		super(repository);
	}


	private List<Schedule> getSchedules(List<Integer> scheduleIds) {
		List<Schedule> result = new ArrayList<>();
		scheduleRepository.findAllById(scheduleIds).forEach(result::add);
		return result;
	}

}
