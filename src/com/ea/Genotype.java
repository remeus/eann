package com.ea;

public class Genotype {
	
	protected int genotypeLength = 0;
	protected BinaryVector genes;
	
	protected Phenotype phenotype;
	
	int fitness = 0; //Be careful if the phenotype changes it should be updated
	
	//Create genotype as a random binary vector and develop its phenotype
	public Genotype(boolean initialize) {
		genotypeLength = Population.testParameters.getFitnessFunction().getNGenes();
		if (initialize) {
			genes = new BinaryVector(genotypeLength); //Create random binary vector
			phenotype = develop();
		}
	}
	
	//Convert genotype into phenotype
	public Phenotype develop() {
		Phenotype pheno = new Phenotype(this);
		fitness = pheno.evaluate(); //Fitness automatically calculated at phenotype's creation
		return pheno;
	}
	
	//Mutate the genotype
	public void mutate() {
		genes.mutate(Population.testParameters.getMutationRate());
		setPhenotype(develop()); //Update the phenotype
	}
	
	//One-point crossover with another genotype, resulting in two children
	public Genotype[] crossover(Genotype secondParent) {
		Genotype[] children = new Genotype[2];
		children[0] = new Genotype(false);
		children[1] = new Genotype(false);
		int crossoverPoint = (int) (genotypeLength * Math.random()); //Crossover point chosen randomly
		children[0].setGenes(genes.crossover(secondParent.getGenes(), Population.testParameters.getCrossoverRate(), crossoverPoint));
		children[1].setGenes(secondParent.getGenes().crossover(genes, Population.testParameters.getCrossoverRate(), crossoverPoint));
		//We must update the phenotypes of the children that are still the same as at creation
		children[0].setPhenotype(children[0].develop());
		children[1].setPhenotype(children[1].develop());
		return children;
	}
	
	
	
	//Getters
	
	public BinaryVector getGenes() {
		return genes;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public int genotypeLength() {
		return genotypeLength;
	}
	
	
	//Setters
	
	public void setGenes(BinaryVector newGenes) {
		genes = newGenes;
	}
	
	public void setPhenotype(Phenotype newPhenotype) {
		phenotype = newPhenotype;
	}
	
	

}
