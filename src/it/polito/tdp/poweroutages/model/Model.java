package it.polito.tdp.poweroutages.model;

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
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {

	private Graph<Nerc, DefaultWeightedEdge> graph;
	private PowerOutagesDAO dao;
	private Map<Integer, Nerc> idMapNerc;
    private Simulator sim ;
	
	public Model() {
		this.idMapNerc = new HashMap<Integer, Nerc>();
		this.dao = new PowerOutagesDAO();
		this.sim = new Simulator();
	}
	
	public void creaGrafo() {
		
		//creo un grafo semplice, pesato e non orientato
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//creo i vertici 
		dao.loadAllNercs(graph,idMapNerc);
		
		//creo gli archi
		dao.loadAllEdges(graph, idMapNerc);
		
		//carico il peso
		dao.loadAllEdgesWeight(graph,idMapNerc);
	}

	public List<Nerc> getNercs() {
		if(graph!=null)
	      	return new ArrayList<Nerc>(graph.vertexSet());
		return null;
	}

	public List<NercWeight> getVicini(Nerc nerc) {
		
		if(graph!=null) {
		
		List<NercWeight> vicini = new LinkedList<>();
		
		for(Nerc vicino : Graphs.neighborListOf(graph, nerc)) {
			
			if(graph.getEdge(nerc, vicino)!=null)
			     vicini.add(new NercWeight(vicino, (int) graph.getEdgeWeight(graph.getEdge(nerc, vicino))));
			else if(graph.getEdge(vicino, nerc)!=null)	
				   vicini.add(new NercWeight(vicino, (int) graph.getEdgeWeight(graph.getEdge(vicino, nerc))));
		}
	           
		Collections.sort(vicini);
		return vicini;
		
		}
		
		return null;
	}

	public void simula(int K) {
		
		
		// al simulatore è necessario passare il numero K e la lista di eventi e il grafo
		PriorityQueue<Event> queue = dao.getAllEvent(idMapNerc);
		sim.init(K, graph, queue);
		sim.run();
		
		
	}
	
	public Stats getStats() {
		return sim.getStatistiche();
	}

}
