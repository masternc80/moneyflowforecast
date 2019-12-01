package com.mff.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForecastRecord {

	private String recordDate;
	
	private double credit;
	
	private double debit;
	
	private String description;
	
	private double balance;
}
