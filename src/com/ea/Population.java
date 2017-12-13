package com.ea;

import com.main.Log;
import com.main.Main;
import com.main.Test;

public class Population {
	
	protected Genotype[] genotypes;
	protected int populationSize;
	
	protected static Test testParameters;
	
	//Initialization if only new genotypes (1st call)
	public Population(Test test) {
		testParameters = test;
		populationSize = test.getMaxPopulationSize();
		genotypes = new Genotype[populationSize];
		for (int i = 0 ; i < populationSize ; i++) {
			genotypes[i] = new Genotype(true);
		}
	}
	
	//Update between two generations (2+ calls)
	public Population(Test test, Genotype[] listGenotypes) {
		populationSize = listGenotypes.length;
		genotypes = new Genotype[populationSize];
		for (int i = 0 ; i < genotypes.length ; i++) {
			genotypes[i] = listGenotypes[i];
		}
	}
	
	
	//Generate the new generation
	public Population nextGeneration() {
		Genotype bestGeno = new Genotype(false);
		bestGeno.setGenes(new BinaryVector(bestGenotype().genes.coordinates));
		bestGeno.develop();
		//Mating pool
		Genotype[] matingPool;
		if (testParameters.getParentSelectionMode() == Test.TOURNAMENT) {
			matingPool = (new Tournament(genotypes)).getMatingPool(); //Initiate the parent selection by tournament
		}
		else {
			matingPool = (new RouletteWheel(genotypes)).getMatingPool(); //Initiate the parent selection by roulette wheel
		}
		
		//Next generation
		Genotype[] nextGeneration = (new AdultSelection(matingPool)).replace(); //Initiate the adult selection with the corresponding mating pool
		//Mutation
		for (int i = 0 ; i < nextGeneration.length ; i++) {
			nextGeneration[i].mutate(); // %% Weird but this line seems to affect also a bit this.genotypes
		}
		//Elitism
		nextGeneration[0] = bestGeno;
		Main.setGenerationsNeeded(Main.generationsNeeded() + 1);
		return new Population(testParameters, nextGeneration);
	}
	
	
	//Return max value of fitness
	public int maxFitness() {
		int maxFitness = genotypes[0].getFitness();
		int currentFitness = 0;
		for (int i = 0 ; i < populationSize ; i++) {
			currentFitness = genotypes[i].getFitness();
			if (currentFitness > maxFitness) {
				maxFitness = currentFitness;
			}
		}
		return maxFitness;
	}
	
	
	//Return best genotype
	public Genotype bestGenotype() {
		return this.genotypes[Tournament.rankMaxFitness(this.genotypes)];
	}
	
	
	//Return average value of fitness
	public double averageFitness() {
		double[] fitness = new double[this.getPopulationSize()];
		double sumFitness = 0;
		for (int j = 0 ; j < this.getPopulationSize() ; j++) {
			fitness[j] = this.genotypes[j].getFitness();
			sumFitness += fitness[j];
		}
		return RouletteWheel.average(fitness, sumFitness);
	}
	
	
	//Return standard deviation in fitness
	public double standardDeviationFitness() {
		double[] fitness = new double[this.getPopulationSize()];
		double sumFitness = 0;
		for (int j = 0 ; j < this.getPopulationSize() ; j++) {
			fitness[j] = this.genotypes[j].getFitness();
			sumFitness += fitness[j];
		}
		return RouletteWheel.standardDeviation(fitness, sumFitness);
	}
	
	
	//Getter
	public int getPopulationSize() {
		return populationSize;
	}
	public static Test testParameters() {
		return testParameters;
	}

}
