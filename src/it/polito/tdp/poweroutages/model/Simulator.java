package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.poweroutages.model.Event.TipoEvento;


public class Simulator {
	
	

	private PriorityQueue<Event> queue;
	private Stats statistiche;
	private int K;
	private Graph <Nerc, DefaultWeightedEdge> graph;
	private Map<Nerc, List<Nerc>> mappaCrediti;
	//Un set che serve a tener conto dei Nerc che aiutano e che non sono liberi
	private Map<Nerc, BonusAiuto> aiutante;
	
	public void init(int k, Graph<Nerc, DefaultWeightedEdge> graph, PriorityQueue<Event> queue) {
		
		//coda con tutti gli eventi
		this.queue = queue;
		//K numero di mesi di credito
		this.K=k;
	
		this.statistiche = new Stats(new ArrayList<Nerc>(graph.vertexSet()));
		this.graph = graph;
		this.mappaCrediti = new HashMap<Nerc, List<Nerc>>();
				
		//inizialmente vuota
		this.aiutante = new HashMap<Nerc, BonusAiuto>();
		
	}

	public void run() {
		
		while(!queue.isEmpty()) {
			
			Event ev = queue.poll();
			Nerc nerc = ev.getNerc();
			
			switch(ev.getTipo()) {
			
			case INTERRUZIONE_SERVIZIO:
				
				// il Nerc chiede aiuto ai vicini
				
				/*
				 *  - Chiede ai debitori  --> si potrebbe costruire una mappa Nerc -> List<Nerc> (debitori)
				 *           dopo un tempo K mesi il debitore viene assolto dal debito - posso creare un evento
				 *  - Chiede al miglior offerente  --> Dato dal peso dell'arco 
				 *  - Chiede ai debitori meglio offerenti
				 *  
				 *  Dobbiamo considerare la catastrofe -- come modellizzare un Nerc occupato? 
				 *  O ci prendiamo bene con le mappe (?) o facciamo un set di liberi(?)
				 */
			
				//il Nerc in difficolta non pu� aiutare nessuno
		
		   
		    nerc.setDisponibile(false);
		
			List<Nerc> debitori = mappaCrediti.get(nerc);
			
			//se non debitori 
			if(debitori != null) {
			
			// controllo se i debitori sono disponibili
			List<Nerc> debDisp = new ArrayList<>();
			
			for(Nerc disp : debitori) {
				
				if(disp.isDisponibile())
					debDisp.add(disp);
				
			}
			
			
		    //ne ho uno
			if(debDisp.size() == 1) {
			
				//prendo il debitore
				Nerc aiuta = debDisp.get(0);
				
				//non � pi� disponibile
				aiuta.setDisponibile(false);
				
				//mappo la relazione   classe che tiene conto della data
				aiutante.put(nerc, new BonusAiuto(aiuta,nerc,ev.getData()));
				break;
			  
			}
			
			//ho p� debitori 
			if(debDisp.size()>1) {
				
					
			         Nerc best = getAiutanteMigliore(nerc, debDisp);
		        	//non � pi� disponibile
		        	best.setDisponibile(false);
			
	        		//mappo la relazione   classe che tiene conto della data
		         	aiutante.put(nerc, new BonusAiuto(best,nerc,ev.getData()));
	          		break;
				
			}
			
		}
			 
			// non ho debitori -- > cerco tra i vicini
			
			List<Nerc> vicini = Graphs.neighborListOf(graph, nerc);
			
			List<Nerc> viciniDisp = new ArrayList<>();
			
			for(Nerc n : vicini) {
				if(n.isDisponibile())
					viciniDisp.add(n);
			}
			
			if(!viciniDisp.isEmpty()) {
			
				Nerc best =  getAiutanteMigliore(nerc, viciniDisp);
				best.setDisponibile(false);
				
        		//mappo la relazione   classe che tiene conto della data
	         	aiutante.put(nerc, new BonusAiuto(best,nerc,ev.getData()));
          		break;
			}
			
			//CATASTROFE 
			statistiche.aggiornaCatastrofi();
			break;
				
			case RIPRISTINO_SERVIZIO:
				
				/*
				 * Libero l'aiutante e il Nerc afflitto 
				 * Do il bonus all'aiutante
				 */
				BonusAiuto ba = aiutante.get(nerc);
				
			// potrebbe esserci stata una catastrofe
				if(ba!=null) {
				
				nerc.setDisponibile(true);
				ba.getDebitore().setDisponibile(true);
				
			
				
				long giorni = Duration.between(ba.getDataDebito().atStartOfDay(), ev.getData().atStartOfDay()).toDays();
				statistiche.aggiornaBonus(ba.getDebitore(), (int) giorni);
				
				//si � creato un debitore
				queue.add(new Event(ev.getData().plusMonths(K), TipoEvento.FINE_CREDITO, ba.getDebitore(), nerc));
				
				}

				
			case FINE_CREDITO:	
				
				// tolgo alla lista
				mappaCrediti.remove(ev.getNerc(), ev.getDebitore());
				
			}
			
			
		}
		
	}

	public Stats getStatistiche() {
		return statistiche;
	}

	private Nerc getAiutanteMigliore(Nerc nerc, List<Nerc> debDisp) {

		if(graph!=null) {
			
			List<NercWeight> vicini = new LinkedList<>();
			
			
			for(Nerc disp :  debDisp) {
				
				if(graph.getEdge(nerc, disp)!=null)
				     vicini.add(new NercWeight(disp, (int) graph.getEdgeWeight(graph.getEdge(nerc, disp))));
				else if(graph.getEdge(disp, nerc)!=null)	
					   vicini.add(new NercWeight(disp, (int) graph.getEdgeWeight(graph.getEdge(disp, nerc))));
			}
		           
			Collections.sort(vicini);
			
			return vicini.get(vicini.size()-1).getNerc();
		
		
			
	}

		return null;
	
 }
	
}
