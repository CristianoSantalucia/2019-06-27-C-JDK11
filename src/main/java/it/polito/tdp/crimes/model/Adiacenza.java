package it.polito.tdp.crimes.model;

public class Adiacenza
{
	private String tipo1; 
	private String tipo2; 
	private Integer peso;
	public Adiacenza(String tipo1, String tipo2, Integer peso)
	{
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
		this.peso = peso;
	}
	public String getTipo1()
	{
		return tipo1;
	}
	public String getTipo2()
	{
		return tipo2;
	}
	public Integer getPeso()
	{
		return peso;
	}
	@Override public String toString()
	{
		return "Adiacenza [tipo1=" + tipo1 + ", tipo2=" + tipo2 + ", peso=" + peso + "]";
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipo1 == null) ? 0 : tipo1.hashCode());
		result = prime * result + ((tipo2 == null) ? 0 : tipo2.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Adiacenza other = (Adiacenza) obj;
		if (tipo1 == null)
		{
			if (other.tipo1 != null) return false;
		}
		else if (!tipo1.equals(other.tipo1)) return false;
		if (tipo2 == null)
		{
			if (other.tipo2 != null) return false;
		}
		else if (!tipo2.equals(other.tipo2)) return false;
		return true;
	}
}
