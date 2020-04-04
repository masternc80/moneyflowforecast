package com.mff.data.dto;

import javax.validation.constraints.NotBlank;

import com.mff.controllers.ScheduleController;
import com.mff.data.entities.RecurringTransaction;
import com.mff.data.entities.Schedule;
import com.mff.repository.ScheduleRepository;
import lombok.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecurringTransactionDto implements ConvertibleDto<RecurringTransaction> {

	private int id;

	@NotBlank
	private String name;

	@NonNull
	private double amount;

	private List<Integer> scheduleIds;

	@Override
	public RecurringTransaction toEntity() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ScheduleRepository scheduleRepository = ScheduleController.getRepository();
		Converter<List<Integer>, List<Schedule>> generateSchedule = ctx ->
		{
			List<Schedule> schedules = new ArrayList<>();
			scheduleRepository.findAllById(ctx.getSource()).forEach(schedules::add);
			return schedules;
		};
		mapper.createTypeMap(NewRecurringTransactionDto.class, RecurringTransaction.class)
				.addMappings(m -> m.using(generateSchedule)
						.map(NewRecurringTransactionDto::getScheduleIds, RecurringTransaction::setSchedules));
		return mapper.map(this, RecurringTransaction.class);
	}
}
