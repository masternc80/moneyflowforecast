package com.mff.data.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mff.data.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements ConvertibleEntity<AccountDto> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	public enum ACCOUNT_TYPE {
		CREDIT_CARD,
		CHECKING,
		SAVINGS,
		CAR_LOAN,
		PERSONAL_LOAN,
		MORTGAGE,
		RETIREMENT,

	};

	private ACCOUNT_TYPE accountType;
	
	@NotEmpty
	private String name;
	
	private double currentBalance;
	
	private double statementBalance;
	
	@NotNull
	private double monthlySpend;
	
    @ManyToOne
    @JoinColumn
	private Schedule schedule;

	@Override
	public AccountDto toDto() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Converter<Schedule, Integer> generateSchedule = ctx -> ctx.getSource().getId();
		mapper.createTypeMap(Account.class, AccountDto.class)
				.addMappings(m -> m.using(generateSchedule)
						.map(Account::getSchedule, AccountDto::setScheduleId));
		return mapper.map(this, AccountDto.class);
	}

}
