package com.mff.data.dto;


import com.mff.controllers.ScheduleController;
import com.mff.data.entities.Account;
import com.mff.data.entities.Schedule;
import com.mff.repository.ScheduleRepository;
import lombok.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements ConvertibleDto<Account> {

	private int id;

	private Account.ACCOUNT_TYPE accountType;

	@NotEmpty
	private String name;

	@NotNull
	private double currentBalance;

	private double statementBalance;

	@NotNull
	private double monthlySpend;

	private int scheduleId;

	@Override
	public Account toEntity() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ScheduleRepository scheduleRepository = ScheduleController.getRepository();
		Converter<Integer, Schedule> generateSchedule = ctx ->
				scheduleRepository.findById(ctx.getSource()).orElse(null);
		mapper.createTypeMap(AccountDto.class, Account.class)
				.addMappings(m -> m.using(generateSchedule)
						.map(AccountDto::getScheduleId, Account::setSchedule));
		return mapper.map(this, Account.class);
	}
}
