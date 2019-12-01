package com.mff.data.dto;


import com.mff.data.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

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
}
