package com.mff.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForecastRecord {

	private long id;

	private String recordDate;

	private String recordDateValue;
	
	private double credit;
	
	private double debit;
	
	private String description;
	
	private double balance;

	private Boolean past;
}
