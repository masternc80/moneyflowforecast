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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

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
	
    @OneToMany
    @JoinColumn
	private List<Transaction> transactions;
}
