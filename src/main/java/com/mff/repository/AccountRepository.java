package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

	public Iterable<Account> findAllByScheduleId(int scheduleId);
	public long countByScheduleId(int scheduleId);
}
