package it.polito.tdp.crimes.model;

import java.time.*;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model
{
	private EventsDao dao ; 
	private Graph<String, DefaultWeightedEdge> grafo; 

	public Model()
	{
		this.dao = new EventsDao(); 
	}

	public void creaGrafo(String category, LocalDate data)
	{
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		//vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(category, data)); 

		//archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(category, data));
		for (Adiacenza a : adiacenze)
			Graphs.addEdge(this.grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
	}
	public List<DefaultWeightedEdge> getInferiori()
	{
		List<DefaultWeightedEdge> archi = new ArrayList<>(); 
		double mediano = this.getMediano(); 
		for(DefaultWeightedEdge e : this.grafo.edgeSet())
		{
			double peso = this.grafo.getEdgeWeight(e);
			if(peso < mediano)
				archi.add(e); 
		} 
		return archi;
	}
	public String stampaInferiori()
	{
		String s = "";
		for(DefaultWeightedEdge e : this.getInferiori())
		{
			String t1 = this.grafo.getEdgeSource(e);
			String t2 = this.grafo.getEdgeTarget(e);
			double peso = this.grafo.getEdgeWeight(e);
			s += String.format("\nArco: %s - %s ( %d )",t1,t2,(int)peso);
		}
		return s; 
	}
	public int getNumVertici()
	{
		return this.grafo.vertexSet().size(); 
	}
	public int getNumArchi()
	{
		return this.grafo.edgeSet().size(); 
	}
	public Collection<String> getCategories()
	{
		return this.dao.getCategories();
	}
	public Collection<LocalDate> getDates()
	{
		return this.dao.getDate();
	}
	
	public double getMediano()
	{
		double max = 0; 
		double min = 1000; 
		for(DefaultWeightedEdge e : this.grafo.edgeSet())
		{
			double peso = this.grafo.getEdgeWeight(e);
			if(peso > max)
				max = peso; 
			else if(peso < min)
				min = peso; 
		}
		return (max+min)/2; 
	}

	public void calcolaPercorso(DefaultWeightedEdge arco)
	{
		String partenza = this.grafo.getEdgeSource(arco);
		String arrivo = this.grafo.getEdgeTarget(arco);
		
		List<String> parziale = new ArrayList<>(); 
		this.cerca(parziale, arrivo);
	}
	
	private void cerca(List<String> parziale, String arrivo)
	{
		//terminale
		if(parziale.get(parziale.size()-1).equals(arrivo))
		{
			
		}
		
		//ricorsione
	}
}
