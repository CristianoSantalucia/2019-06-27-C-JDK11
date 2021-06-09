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

	private String partenza;
	private String arrivo;
	private List<Parziale> percorso;
	private double pesoMax; 

	public List<Parziale> calcolaPercorso(DefaultWeightedEdge arco)
	{
		partenza = this.grafo.getEdgeSource(arco);
		arrivo = this.grafo.getEdgeTarget(arco);

		List<Parziale> parziale = new ArrayList<>(); 

		parziale.add(new Parziale(partenza, 0)); 
		this.cerca(parziale, arrivo);

		return percorso; 
	}

	private void cerca(List<Parziale> parziale, String arrivo)
	{
		//terminale
		if(parziale.get(parziale.size()-1).getString().equals(arrivo))
		{
			double peso = 0;
			for (Parziale p : parziale)
				peso += p.getPeso(); 
			if(peso > pesoMax)
				percorso = new ArrayList<>(parziale);
			return;
		}
		//ricorsione
		for(String n : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1).getString()))
		{
			DefaultWeightedEdge edge = this.grafo.getEdge(parziale.get(parziale.size()-1).getString(), n);
			if(edge != null)
			{
				double peso = this.grafo.getEdgeWeight(edge);
				Parziale par = new Parziale(arrivo, peso);
				parziale.add(par); 
				this.cerca(parziale, arrivo);
				parziale.remove(par);
			}
		}
	}
}
