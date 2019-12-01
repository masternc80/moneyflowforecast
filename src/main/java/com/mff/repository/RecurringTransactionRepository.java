package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.RecurringTransaction;

public interface RecurringTransactionRepository extends CrudRepository<RecurringTransaction, Integer> {
	
	public Iterable<RecurringTransaction> findAllByScheduleId(int scheduleId);

}
