package com.ea;

public class ParentSelection {

	protected Genotype[] matingPool;
	protected int sizeMatingPool;
	
	public ParentSelection(Genotype[] population) {
		
		sizeMatingPool = population.length;
		matingPool = new Genotype[sizeMatingPool]; //Pool same size as population
		
	}
	
	//Getter
	public Genotype[] getMatingPool() {
		return this.matingPool;
	}
	
}
