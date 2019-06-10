package it.polito.tdp.poweroutages.db;

import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Event;
import it.polito.tdp.poweroutages.model.Event.TipoEvento;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public void loadAllNercs(Graph<Nerc, DefaultWeightedEdge> graph, Map<Integer, Nerc> idMapNerc) {

		String sql = "SELECT id, value FROM nerc";
		

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
			
				if(!idMapNerc.containsKey(res.getInt("id"))) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				idMapNerc.put(res.getInt("id"), n);
				graph.addVertex(n);
				}
			}

			conn.close();
			

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		

	
	}

	public void loadAllEdgesWeight(Graph<Nerc, DefaultWeightedEdge> graph, Map<Integer, Nerc> idMapNerc) {
		
	String sql = "select DISTINCT n.nerc_one, n.nerc_two,COUNT(DISTINCT year(p1.date_event_began), month(p1.date_event_began)) as peso " + 
			"from poweroutages p1, poweroutages p2, nercrelations n " + 
			"where p1.nerc_id=n.nerc_one and p2.nerc_id=n.nerc_two and month(p1.date_event_began)=month(p2.date_event_began) " + 
			"and year(p1.date_event_began)=year(p2.date_event_began) " + 
			"Group BY n.nerc_one, n.nerc_two ";
		

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
			
				if(idMapNerc.containsKey(res.getInt("n.nerc_one"))&&idMapNerc.containsKey(res.getInt("n.nerc_two"))) {
				
					Nerc primo = idMapNerc.get(res.getInt("n.nerc_one"));
					Nerc secondo = idMapNerc.get(res.getInt("n.nerc_two"));
					
					if(graph.getEdge(primo, secondo)!=null)
				         graph.setEdgeWeight(graph.getEdge(primo, secondo), res.getInt("peso"));
					else if(graph.getEdge(secondo,primo )!=null)
						graph.setEdgeWeight(graph.getEdge(secondo,primo), res.getInt("peso"));
				}
			}

			conn.close();
			

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		

	
		
	}

	public void loadAllEdges(Graph<Nerc, DefaultWeightedEdge> graph, Map<Integer, Nerc> idMapNerc) {
		String sql = "SELECT nerc_one, nerc_two FROM nercrelations ";
			

			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();

				while (res.next()) {
				
					if(idMapNerc.containsKey(res.getInt("nerc_one")) && idMapNerc.containsKey(res.getInt("nerc_two"))) {
					
						Nerc primo = idMapNerc.get(res.getInt("nerc_one"));
						Nerc secondo = idMapNerc.get(res.getInt("nerc_two"));
						
						Graphs.addEdge(graph, primo, secondo, 0);
						
					}
				}

				conn.close();
				

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
	}

	public PriorityQueue<Event> getAllEvent(Map<Integer, Nerc> idMapNerc) {
		
		String sql = "SELECT  p.nerc_id, p.date_event_began, p.date_event_finished FROM  poweroutages p";
		PriorityQueue<Event> queue = new PriorityQueue<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
			
				if(idMapNerc.containsKey(res.getInt("p.nerc_id"))) {
					
					queue.add(new Event(res.getDate("p.date_event_began").toLocalDate(), TipoEvento.INTERRUZIONE_SERVIZIO, idMapNerc.get(res.getInt("p.nerc_id"))));
					queue.add(new Event(res.getDate("p.date_event_finished").toLocalDate(), TipoEvento.RIPRISTINO_SERVIZIO, idMapNerc.get(res.getInt("p.nerc_id"))));
				}
					
			}

			conn.close();
            return queue;
			

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
