package com.mff.data.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class RecurringTransactionDto {
	
	@NonNull
	private int dayOfMonth;
	
	private boolean credit;
	
	@NonNull
	private double amount;
	
	@NotBlank
	private String description;
	
	private boolean priorWeekend;

}
