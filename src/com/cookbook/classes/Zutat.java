package com.cookbook.classes;

public class Zutat {
	
	//properties are immutable - Can not change after object creation
	private final String name;
	private final double menge;
	private final String einheit;

	public Zutat(String name, double menge, String einheit) {
		this.name = name;
		this.menge = menge;
		this.einheit = einheit;
	}
	
	public String getName() {
		return name;
	}
	
	public Double getMenge() {
		return menge;
	}
	
	public String getEinheit() {
		return einheit;
	}

}
