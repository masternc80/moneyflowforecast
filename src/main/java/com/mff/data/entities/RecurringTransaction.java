package com.mff.data.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecurringTransaction {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
    @ManyToOne
    @JoinColumn
	private Schedule schedule;
	
	private boolean credit;
	
	private double amount;
	
	private String description;

}
