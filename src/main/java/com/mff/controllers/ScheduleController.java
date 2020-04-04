package com.mff.controllers;

import com.mff.data.dto.NewScheduleDto;
import com.mff.data.dto.ScheduleDto;
import com.mff.data.entities.Schedule;
import com.mff.repository.AccountRepository;
import com.mff.repository.RecurringTransactionRepository;
import com.mff.repository.ScheduleRepository;
import com.mff.util.ScheduleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("schedule")
public class ScheduleController extends AbstractController<Schedule, ScheduleDto, NewScheduleDto, ScheduleRepository> {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;

    private static ScheduleController INSTANCE;

    ScheduleController(ScheduleRepository repository) {
        super(repository);
        INSTANCE = this;
    }

    public static ScheduleRepository getRepository() {
        return INSTANCE.repository;
    }

    public static ScheduleFactory getFactory() {
        return INSTANCE.scheduleFactory;
    }

    public static AccountRepository getAccountRepository() {
        return INSTANCE.accountRepository;
    }

    public static RecurringTransactionRepository getRecurringTransactionRepository() {
        return INSTANCE.recurringTransactionRepository;
    }
}
