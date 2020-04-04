package com.mff.data.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mff.data.dto.ConvertibleDto;
import com.mff.data.dto.TransactionDto;
import lombok.Data;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Data
@Entity
public class Transaction implements ConvertibleDto<TransactionDto> {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@ManyToOne
    @JoinColumn
	private Account account;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private boolean credit;
	
	private double amount;
	
	private String description;

	@Override
	public TransactionDto toEntity() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Converter<Account, String> convertAccount = ctx ->
				Integer.toString(ctx.getSource().getId());
		mapper.createTypeMap(Transaction.class, TransactionDto.class)
				.addMappings(m -> m.using(convertAccount)
						.map(Transaction::getAccount, TransactionDto::setAccountId));
		return mapper.map(this, TransactionDto.class);
	}
}
