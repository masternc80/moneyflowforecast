package com.mff.data.entities;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Holiday {
	@Column
	@NotNull
	private int year;
	
	@Id
	@Column(name="HOLIDAY_DATE")
	@Temporal(TemporalType.DATE)
	private Date date;
}
