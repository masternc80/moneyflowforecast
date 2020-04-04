package com.mff.controllers;

import com.mff.data.dto.AccountDto;
import com.mff.data.dto.CountDto;
import com.mff.data.dto.NewAccountDto;
import com.mff.data.entities.Account;
import com.mff.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController extends AbstractController<Account, AccountDto, NewAccountDto, AccountRepository> {

	AccountController(AccountRepository repository) {
		super(repository);
	}

	@GetMapping("/schedule/{id}")
	private ResponseEntity<CountDto> countBySchedule(@PathVariable int id) {
		long count = repository.countByScheduleId(id);
		return ResponseEntity.ok(CountDto.builder().count(count).build());
	}
}
