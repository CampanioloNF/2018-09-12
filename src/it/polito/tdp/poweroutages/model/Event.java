package it.polito.tdp.poweroutages.model;

import java.time.LocalDate;


public class Event implements Comparable<Event> {

	
	//Un evento altro non è che un poweroutages
	
    /*
     * Sostanzialmente ai nostri fini sono necessari
     *   - la data
     *   - il nerc 
     *   - il tipo di vento
     */
	
	public enum TipoEvento{
		
		INTERRUZIONE_SERVIZIO,
		RIPRISTINO_SERVIZIO, 
		FINE_CREDITO
		
	}
	
	private LocalDate data;
	private TipoEvento tipo;
	private Nerc nerc;
	private Nerc debitore;
	
	public Event(LocalDate data, TipoEvento tipo, Nerc nerc) {
		super();
		this.data = data;
		this.tipo = tipo;
		this.nerc = nerc;
	}
	
	public Event(LocalDate data, TipoEvento tipo, Nerc creditore, Nerc debitore) {
		super();
		this.data = data;
		this.tipo = tipo;
		this.nerc = creditore;
		this.debitore = debitore;
	}
	



	public LocalDate getData() {
		return data;
	}





	public TipoEvento getTipo() {
		return tipo;
	}





	public Nerc getNerc() {
		return nerc;
	}





	public Nerc getDebitore() {
		return debitore;
	}

	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return 0;
	}





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((nerc == null) ? 0 : nerc.hashCode());
		return result;
	}





	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (nerc == null) {
			if (other.nerc != null)
				return false;
		} else if (!nerc.equals(other.nerc))
			return false;
		return true;
	}

}
