package com.ea;

import java.util.Arrays;

import com.main.Test;

public class RouletteWheel extends ParentSelection {

	
	public RouletteWheel(Genotype[] population) {
		
		super(population);
		selectCompetitors(population);
		
	}
	
	
	//Initialize the mating pool
	public void selectCompetitors(Genotype[] pop) {
		int length = pop.length;
		double[] fitness = new double[length];
		double[] proportion = new double[length];
		double sumFitness = 0;
		for (int i = 0 ; i < length ; i++) { //Get fitnesses
			fitness[i] = (double)pop[i].getFitness();
			sumFitness += fitness[i];
		}
		if (length == 0)
			throw new IllegalArgumentException("Roulette can't be launch since population is null.");
		else if (sumFitness == 0)
			throw new IllegalArgumentException("Roulette can't be launch since sum fitness is null.");
		else {
			//We update the proportions of the wheel given to each adult
			if (Population.testParameters.getParentSelectionMode() == Test.FITNESS_PROPORTIONATE)
				fitnessProportionateInitialization(proportion, fitness, sumFitness);
			else if (Population.testParameters.getParentSelectionMode() == Test.SIGMA_SCALING)
				sigmaScalingInitialization(proportion, fitness, sumFitness);
			else if (Population.testParameters.getParentSelectionMode() == Test.RANK)
				rankInitialization(proportion, fitness, sumFitness);
			else
				throw new IllegalArgumentException("This mode is not supported for parent selection.");
			//We play roulette wheel and fill the mating pool with each parent
			for (int j = 0 ; j < sizeMatingPool ; j++) {
				int value = launchRouletteWheel(proportion, j);
				matingPool[j] = pop[value]; //The genotype chosen is put in the pool
			}
		}
	}
	
	
	
	//Launch roulette wheel and return the value
	public int launchRouletteWheel(double[] proportion, int j) {
			double valueRoulette = Math.random(); //Launch roulette
			int i = 0;
			while (proportion[i] < valueRoulette && i < proportion.length - 1) {
				i++;
				valueRoulette -= proportion[i]; //We don't consider the previous interval anymore
			}
			return i;
	}
	
	
	//Fitness proportionate calculation
	public void fitnessProportionateInitialization(double[] proportion, double[] fitness, double sumFitness) {
		for (int i = 0 ; i < proportion.length ; i++) {
			proportion[i] = fitness[i] / sumFitness;
		}
	}
	
	
	//Sigma scaling calculation
	public void sigmaScalingInitialization(double[] proportion, double[] fitness, double sumFitness) {
		for (int i = 0 ; i < proportion.length ; i++) {
			double sD = standardDeviation(fitness, sumFitness);
			if (sD == 0)
				proportion[i] = 1;
			else
				proportion[i] = 1 + (fitness[i] - average(fitness, sumFitness)) / (2 * sD);
		}
		normalize(proportion);
	}
		
		
	//Rank calculation
	public void rankInitialization(double[] proportion, double[] fitness, double sumFitness) {
		int length = fitness.length;
		double[] sortFitness = fitness.clone();
		Arrays.sort(sortFitness);
		double min = sortFitness[0];
		double max = sortFitness[length - 1];
		for (int i = 0 ; i < proportion.length ; i++) {
			proportion[i] = min + (max - min) * (getRank(sortFitness, fitness[i]) - 1) / (length - 1);
		}
		normalize(proportion);
	}	
	
	
	//Average
	public static double average(double[] fitness, double sumFitness) {
		return (sumFitness / fitness.length);
	}
	
	
	//Variance
	public static double variance(double[] fitness, double sumFitness) {
		int length = fitness.length;
		double[] squareFitness = new double[length];
		double squareSumFitness = 0;
		for (int i = 0 ; i < length ; i++) {
			squareFitness[i] = fitness[i] * fitness[i];
			squareSumFitness += squareFitness[i];
		}
		double eX = average(fitness, sumFitness);
		double eX2 = average(squareFitness, squareSumFitness);
		return (eX2 - eX * eX);
	}
	
	
	//Standard deviation
	public static double standardDeviation(double[] fitness, double sumFitness) {
		return Math.sqrt(variance(fitness, sumFitness));
	}
	
	
	//Normalize an array
	public static void normalize(double[] array) {
		int length = array.length;
		int sumArray = 0;
		for (int i = 0 ; i < length ; i++) {
			sumArray += array[i];
		}
		for (int i = 0 ; i < length ; i++)
			array[i] /= sumArray;
	}
	
	
	//Get rank of a given value
	public static int getRank(double[] sortFitness, double value) {
		int rank = -1;
		for (int i = 0 ; i < sortFitness.length ; i++) {
			if (sortFitness[i] == value && rank == -1) //If there is a tie, we keep the first value
				rank = i;
		}
		return rank;
	}
	
}
