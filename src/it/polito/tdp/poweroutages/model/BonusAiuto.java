package it.polito.tdp.poweroutages.model;

import java.time.LocalDate;

public class BonusAiuto {

	private Nerc debitore;
	private Nerc creditore;
	private LocalDate dataDebito;
	
	public BonusAiuto(Nerc debitore, Nerc creditore, LocalDate dataDebito) {
		super();
		this.debitore = debitore;
		this.creditore = creditore;
		this.dataDebito = dataDebito;
	}
	public Nerc getDebitore() {
		return debitore;
	}
	public Nerc getCreditore() {
		return creditore;
	}
	public LocalDate getDataDebito() {
		return dataDebito;
	}
	
	
	
}
