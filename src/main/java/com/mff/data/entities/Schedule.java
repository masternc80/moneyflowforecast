package com.mff.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
	
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
	
}
