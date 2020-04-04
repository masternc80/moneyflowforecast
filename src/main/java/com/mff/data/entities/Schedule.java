package com.mff.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.mff.controllers.ScheduleController;
import com.mff.data.dto.ScheduleDto;
import com.mff.repository.AccountRepository;
import com.mff.repository.RecurringTransactionRepository;
import com.mff.util.ScheduleFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule implements ConvertibleEntity<ScheduleDto> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@NotNull
	private String name;

	@NotNull
	private Boolean isPeriodic;

	public enum PERIOD_UNIT {
		DAY,
		WEEK,
		MONTH
	};

	private Integer periodLength;

	private PERIOD_UNIT periodUnit;

	private Date startDate;

	private Integer baseDayOfMonth;

	@NotNull
	private Boolean isPriorWeekend;

	@Override
	public ScheduleDto toDto() {
		ModelMapper mapper = new ModelMapper();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		ScheduleFactory scheduleFactory = ScheduleController.getFactory();
		AccountRepository accountRepository = ScheduleController.getAccountRepository();
		RecurringTransactionRepository recurringTransactionRepository = ScheduleController.getRecurringTransactionRepository();
		Converter<Integer, Date> generateNextDate = ctx ->
				scheduleFactory.generateDate(this, gregorianCalendar.get(Calendar.MONTH), gregorianCalendar.get(Calendar.YEAR));
		Converter<Integer, Boolean> generateCanDelete = ctx ->
				accountRepository.countByScheduleId(ctx.getSource()) == 0
						&& recurringTransactionRepository.countSchedulesById(ctx.getSource()) == 0;
		mapper.createTypeMap(Schedule.class, ScheduleDto.class)
				.addMappings(m -> {
					m.using(generateNextDate)
							.map(Schedule::getId, ScheduleDto::setNextDate);
					m.using(generateCanDelete)
							.map(Schedule::getId, ScheduleDto::setCanDelete);
				});
		return mapper.map(this, ScheduleDto.class);
	}
}
