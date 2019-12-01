package com.mff.controllers;

import com.mff.data.dto.CountDto;
import com.mff.data.dto.StatusDto;
import com.mff.data.entities.Schedule;
import com.mff.repository.ScheduleRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mff.data.dto.AccountDto;
import com.mff.data.entities.Account;
import com.mff.repository.AccountRepository;
import com.mff.util.ScheduleFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ScheduleFactory scheduleFactory;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@GetMapping
	private Iterable<AccountDto> getAll() {
		List<AccountDto> accounts = new ArrayList<>();
		accountRepository.findAll().forEach(s -> accounts.add(toDto(s)));
		return accounts;
	}

	@GetMapping("/{id}")
	private ResponseEntity<AccountDto> get(@PathVariable int id) {
		return accountRepository.findById(id).map(entity -> ResponseEntity.ok(toDto(entity))).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/schedule/{id}")
	private ResponseEntity<CountDto> countBySchedule(@PathVariable int id) {
		long count = accountRepository.countByScheduleId(id);
		return ResponseEntity.ok(CountDto.builder().count(count).build());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto dto) throws URISyntaxException {
		AccountDto accountDto = toDto(accountRepository.save(toEntity(dto)));
		return ResponseEntity.created(new URI("/" + accountDto.getId())).body(accountDto);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseEntity<AccountDto> create(@PathVariable int id, @RequestBody AccountDto dto) {
		dto.setId(id);
		return ResponseEntity.ok(toDto(accountRepository.save(toEntity(dto))));
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<StatusDto> delete(@PathVariable int id) {
		if (accountRepository.existsById(id)) {
			accountRepository.deleteById(id);
			return ResponseEntity.ok(StatusDto.builder().status("OK").build());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private Account toEntity(AccountDto dto) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Converter<Integer, Schedule> generateSchedule = ctx ->
				mapper.map(scheduleRepository.findById(ctx.getSource()).orElse(null), Schedule.class);
		mapper.createTypeMap(AccountDto.class, Account.class)
				.addMappings(m -> m.using(generateSchedule)
							.map(AccountDto::getScheduleId, Account::setSchedule));
		return mapper.map(dto, Account.class);
	}

	private AccountDto toDto(Account entity) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Converter<Schedule, Integer> generateSchedule = ctx ->
				mapper.map(ctx.getSource().getId(), Integer.class);
		mapper.createTypeMap(Account.class, AccountDto.class)
				.addMappings(m -> m.using(generateSchedule)
						.map(Account::getSchedule, AccountDto::setScheduleId));
		return mapper.map(entity, AccountDto.class);
	}

}
