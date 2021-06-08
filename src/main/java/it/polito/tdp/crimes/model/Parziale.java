package it.polito.tdp.crimes.model;

public class Parziale
{
	private String string; 
	private double peso;
	
	public Parziale(String string, double peso)
	{
		this.string = string;
		this.peso = peso;
	}

	public String getString()
	{
		return string;
	}

	public double getPeso()
	{
		return peso;
	}

	@Override public String toString()
	{
		return "Parziale [string=" + string + ", peso=" + peso + "]\n";
	}
}
