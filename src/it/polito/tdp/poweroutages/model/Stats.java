package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Stats {

	// classe che contiene le statistiche relative a tutti i nerc
	private Map<Nerc, Integer> bonus;
	private int catastrofi;
	
	public Stats(List<Nerc> list) {
		
		this.bonus = new HashMap<Nerc, Integer>();
		
		//inizialmente non abbiamo bonus
		for(Nerc nerc : list)
			bonus.put(nerc, 0);
		
		//ne catastrofi
		this.catastrofi = 0;
	}
	
	public void aggiornaCatastrofi() {
		this.catastrofi++;
	}
	
	public void aggiornaBonus(Nerc nerc, Integer bonus) {
		int prima = this.bonus.get(nerc);
		this.bonus.replace(nerc, prima+bonus);
	}

	public int getCatastrofi() {
		return catastrofi;
	}

	
	// poi ci sarà un metodo toString
	public String toString() {
		
		String res = "";
		
		    res+="Numero catastrofi: "+catastrofi+"\n";
            res+="Lista dei Nerc e relativi bonus\n\n";
		    for(Entry<Nerc, Integer> entry : bonus.entrySet())
		    	res+=entry.getKey()+"  "+entry.getValue()+"\n";
		    
		 return res;   
		 }
}
