package com.problem;

import com.main.Test;

public abstract class FitnessFunction {

	protected int nGenes = 0;
	
	//Constructor
	public FitnessFunction(int genes) {
		nGenes = genes;
	}

	
	//Fitness function
	public abstract int fitness(int[] vector);
	
	
	//Getter
	public int getNGenes() {
		return nGenes;
	}
	
	
	//Fetch best set of parameters for a given mode
	public static Test bestParameters(int evolutionMode, int nScenarios) {
		
		Test testParametersS1 = new Test(
				100, //nGenerations,
				50, //sizePop,
				0.015, //mutationRate,
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,
				Test.TOURNAMENT, //parentSelectionMode,
				0.3, //epsilon,
				0.05, //pK,
				new FlatlandFitnessFunction(
						new int[]{
							6, //Input layer
							6, //Hidden layer 1
							3  //Output layer
						}, //ANN structure
						8, //Encoding size
						1 / Math.sqrt(6), //Allowed range for weights
						(6 * 6 + 6 * 3) * 8, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						FlatlandFitnessFunction.STATIC, //Static or dynamic mode
						1 //Number of scenarios per genotype
					)
			);
		
		Test testParametersD5 = new Test(
				100, //nGenerations,
				50, //sizePop,
				0.0015, //mutationRate,
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,
				Test.SIGMA_SCALING, //parentSelectionMode,
				0.3, //epsilon, decrease social pressure
				0.1, //pK, increase social pressure
				new FlatlandFitnessFunction(
						new int[]{
								6, //Input layer
								10, //Hidden layer 1
								3  //Output layer
							}, //ANN structure
						8, //Encoding size
						1 / Math.sqrt(6), //Allowed range for weights
						(6*10+3*10)*8, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						FlatlandFitnessFunction.DYNAMIC, //Static or dynamic mode
						5 //Number of scenarios per genotype
					)
			);
		
		Test testParametersD1 = new Test(
				200, //nGenerations,
				40, //sizePop,
				0.0015, //mutationRate,
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,
				Test.SIGMA_SCALING, //parentSelectionMode,
				0.7, //epsilon, decrease social pressure
				0.2, //pK, increase social pressure
				new FlatlandFitnessFunction(
						new int[]{
								6, //Input layer
								6, //Hidden layer 1
								3  //Output layer
							}, //ANN structure
						8, //Encoding size
						1 / Math.sqrt(6), //Allowed range for weights
						(6*6+3*6)*8, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						FlatlandFitnessFunction.DYNAMIC, //Static or dynamic mode
						1 //Number of scenarios per genotype
					)
			);
		
		Test testParametersS5 = new Test(
				100, //nGenerations,
				50, //sizePop,
				0.015, //mutationRate,
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,
				Test.TOURNAMENT, //parentSelectionMode,
				0.3, //epsilon,
				0.05, //pK,
				new FlatlandFitnessFunction(
						new int[]{
							6, //Input layer
							6, //Hidden layer 1
							3  //Output layer
						}, //ANN structure
						8, //Encoding size
						1 / Math.sqrt(6), //Allowed range for weights
						(6 * 6 + 6 * 3) * 8, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						FlatlandFitnessFunction.STATIC, //Static or dynamic mode
						5 //Number of scenarios per genotype
					)
			);
		
		
		
		switch(evolutionMode) {
			case FlatlandFitnessFunction.STATIC:
				switch(nScenarios) {
					case 1:
						return testParametersS1;
					case 5:
						return testParametersS5;
					default:
						throw new IllegalArgumentException("No saved parameters with that number of scenarios");
				}
			case FlatlandFitnessFunction.DYNAMIC:
				switch(nScenarios) {
					case 1:
						return testParametersD1;
					case 5:
						return testParametersD5;
					default:
						throw new IllegalArgumentException("No saved parameters with that number of scenarios");
				}
			default:
				throw new IllegalArgumentException("This evolution mode is not known");
		}
	}
	
	//Get best set of parameters
	public static Test bestParameters() {
		
		//STATIC SAME SCENARIO AT THE END
		Test bestParameters = new Test(
				100, //nGenerations,
				50, //sizePop,
				0.015, //mutationRate,
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,
				Test.TOURNAMENT, //parentSelectionMode,
				0.4, //epsilon, decrease social pressure
				1, //pK, increase social pressure
				new FlatlandFitnessFunction(
						new int[]{
								6, //Input layer
								6, //Hidden layer 1
								3  //Output layer
							}, //ANN structure
						8, //Encoding size
						1 / Math.sqrt(6), //Allowed range for weights
						(6*6+3*6)*8, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						FlatlandFitnessFunction.DYNAMIC, //Static or dynamic mode
						2 //Number of scenarios per genotype
					)
				);
				
		return bestParameters;
		
	}
	
}
