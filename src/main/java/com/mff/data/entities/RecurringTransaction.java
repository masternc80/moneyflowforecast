package com.mff.data.entities;

import javax.persistence.*;

import com.mff.data.dto.RecurringTransactionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecurringTransaction implements ConvertibleEntity<RecurringTransactionDto> {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	@ManyToMany
    @JoinColumn
	private List<Schedule> schedules;

	private double amount;

	@Override
	public RecurringTransactionDto toDto() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Converter<List<Schedule>, List<Integer>> generateScheduleIds = ctx ->
				ctx.getSource().stream().map(Schedule::getId).collect(Collectors.toList());
		mapper.createTypeMap(RecurringTransaction.class, RecurringTransactionDto.class)
				.addMappings(m -> m.using(generateScheduleIds)
						.map(RecurringTransaction::getSchedules, RecurringTransactionDto::setScheduleIds));
		return mapper.map(this, RecurringTransactionDto.class);
	}
}
