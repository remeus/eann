package com.main;

import java.util.Arrays;
//import java.util.function.Function;

import com.problem.FitnessFunction;

public class Test {

	protected int maxNumberOfGenerations;
	protected int maxPopulationSize;
	protected double mutationRate;
	protected double crossoverRate;
	protected int adultSelectionMode;
	protected int parentSelectionMode;
	protected double epsilon;
	protected double pK;
	protected FitnessFunction fitnessFunction;
	
	public static final int FULL = 1,
							OVER_PRODUCTION = 2,
							MIXING = 3;
	public static final int[] supportedAdultSelectionMode = {FULL, OVER_PRODUCTION, MIXING}; //Must be sorted
	
	public static final int FITNESS_PROPORTIONATE = 1, 
							SIGMA_SCALING = 2, 
							RANK = 3, 
							TOURNAMENT = 4;
	public static final int[] supportedParentSelectionMode = {FITNESS_PROPORTIONATE, SIGMA_SCALING, RANK, TOURNAMENT}; //Must be sorted
	
	
	//Constructor
	
	
	public Test(int generations, int size, double mutation, double crossover, int adultSelection, int parentSelection, double epsilon, double pK, FitnessFunction fun) {
		
		setMaxNumberOfGenerations(generations);
		setMaxPopulationSize(size);
		setMutationRate(mutation);
		setCrossoverRate(crossover);
		setAdultSelectionMode(adultSelection);
		setParentSelectionMode(parentSelection);
		setEpsilon(epsilon);
		setPK(pK);
		fitnessFunction = fun;
		
	}
	
	
	
	//Getters
	
	
	/**
	 * @return the maxNumberOfGenerations
	 */
	public int getMaxNumberOfGenerations() {
		return maxNumberOfGenerations;
	}






	/**
	 * @return the maxPopulationSize
	 */
	public int getMaxPopulationSize() {
		return maxPopulationSize;
	}






	/**
	 * @return the mutationRate
	 */
	public double getMutationRate() {
		return mutationRate;
	}






	/**
	 * @return the crossoverRate
	 */
	public double getCrossoverRate() {
		return crossoverRate;
	}






	/**
	 * @return the adultSelectionMode
	 */
	public int getAdultSelectionMode() {
		return adultSelectionMode;
	}






	/**
	 * @return the parentSelectionMode
	 */
	public int getParentSelectionMode() {
		return parentSelectionMode;
	}
	
	
	public String getAdultSelectionModeName() {
		switch (adultSelectionMode) {
		case Test.FULL:
			return "Full";
		case Test.OVER_PRODUCTION:
			return "Over production";
		case Test.MIXING:
			return "Mixing";
		default:
			return "";
		}
	}
	
	
	public String getParentSelectionModeName() {
		switch (parentSelectionMode) {
		case Test.FITNESS_PROPORTIONATE:
			return "Fitness proportionate";
		case Test.SIGMA_SCALING:
			return "Sigma-scaling";
		case Test.RANK:
			return "Rank";
		case Test.TOURNAMENT:
			return "Tournament";
		default:
			return "";
		}
	}
	
	public double getEpsilon() {
		return epsilon;
	}
	
	public double getPK() {
		return pK;
	}
	
	
	public String getFitnessFunctionName() {
		return fitnessFunction.getClass().getSimpleName();
	}
	
	public FitnessFunction getFitnessFunction() {
		return fitnessFunction;
	}




	//Setters


	/**
	 * @param maxNumberOfGenerations the maxNumberOfGenerations to set
	 */
	public void setMaxNumberOfGenerations(int maxNumberOfGenerations) {
		if (maxNumberOfGenerations > 0)
			this.maxNumberOfGenerations = maxNumberOfGenerations;
		else
			throw new IllegalArgumentException("The maximum number of generations cannot be null.");
	}






	/**
	 * @param maxPopulationSize the maxPopulationSize to set
	 */
	public void setMaxPopulationSize(int maxPopulationSize) {
		this.maxPopulationSize = maxPopulationSize;
	}






	/**
	 * @param mutationRate the mutationRate to set
	 */
	public void setMutationRate(double mutationRate) {
		if (mutationRate >= 0 && mutationRate <= 1)
			this.mutationRate = mutationRate;
		else
			throw new IllegalArgumentException("Invalid mutation rate");
	}






	/**
	 * @param crossoverRate the crossoverRate to set
	 */
	public void setCrossoverRate(double crossoverRate) {
		if (crossoverRate >= 0 && crossoverRate <= 1)
			this.crossoverRate = crossoverRate;
		else
			throw new IllegalArgumentException("Invalid crossover rate");
	}
	
	
	/**
	 * @param epsilon the epsilon to set
	 */
	public void setEpsilon(double epsilon) {
		if (epsilon >= 0 && epsilon <= 1)
			this.epsilon = epsilon;
		else
			throw new IllegalArgumentException("Invalid epsilon");
	}
	
	
	/**
	 * @param pK the pK to set
	 */
	public void setPK(double pK) {
		if (pK >= 0 && pK <= 1)
			this.pK = pK;
		else
			throw new IllegalArgumentException("Invalid pK");
	}






	/**
	 * @param adultSelectionMode the adultSelectionMode to set
	 */
	public void setAdultSelectionMode(int adultSelection) {
		if (Arrays.binarySearch(supportedAdultSelectionMode, adultSelection) >= 0)
			this.adultSelectionMode = adultSelection;
		else
			throw new IllegalArgumentException("Adult selection mode not supported");
	}






	/**
	 * @param parentSelectionMode the parentSelectionMode to set
	 */
	public void setParentSelectionMode(int parentSelection) {
		if (Arrays.binarySearch(supportedParentSelectionMode, parentSelection) >= 0)
			this.parentSelectionMode = parentSelection;
		else
			throw new IllegalArgumentException("Parent selection mode not supported");
	}




	
}
