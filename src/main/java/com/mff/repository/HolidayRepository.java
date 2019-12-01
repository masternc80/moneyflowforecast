package com.mff.repository;

import org.springframework.data.repository.CrudRepository;

import com.mff.data.entities.Holiday;

public interface HolidayRepository extends CrudRepository<Holiday, Integer> {

}
