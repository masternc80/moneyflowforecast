package com.mff.repository;

import com.mff.data.entities.Schedule;
import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.RecurringTransaction;

public interface RecurringTransactionRepository extends CrudRepository<RecurringTransaction, Integer> {
	
	Iterable<RecurringTransaction> findAllBySchedulesIdIs(int scheduleId);
    long countSchedulesById(int id);
}
