package com.cookbook.classes;

public class Zutat {
	
	//properties are immutable - Can not change after object creation
	private String name;
	private double menge;
	private String einheit;

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
	
	public void addMenge(double newMenge){
		this.menge = menge + newMenge;
	}
	
	public void delMenge(double newMenge){
		this.menge = menge - newMenge;
	}

}
