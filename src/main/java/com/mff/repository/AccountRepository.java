package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

	Iterable<Account> findAllByScheduleId(int scheduleId);
	long countByScheduleId(int scheduleId);
}
