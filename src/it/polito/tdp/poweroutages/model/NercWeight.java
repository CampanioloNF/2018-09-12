package it.polito.tdp.poweroutages.model;

public class NercWeight implements Comparable<NercWeight>{

	private Nerc nerc;
	private int peso;
	
	public NercWeight(Nerc nerc, int peso) {
		
		this.nerc = nerc;
		this.peso = peso;
	}

	public Nerc getNerc() {
		return nerc;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		NercWeight other = (NercWeight) obj;
		if (nerc == null) {
			if (other.nerc != null)
				return false;
		} else if (!nerc.equals(other.nerc))
			return false;
		return true;
	}

	@Override
	public int compareTo(NercWeight o) {
		// TODO Auto-generated method stub
		return o.peso-this.peso;
	}
	
	
}
