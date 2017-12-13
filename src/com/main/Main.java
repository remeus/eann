package com.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import com.ea.Population;
import com.flatland.Window;
import com.problem.ANN;
import com.problem.FitnessFunction;
import com.problem.FlatlandFitnessFunction;
import com.problem.Simulation;

public class Main {

	protected static XYSeriesCollection dataset = new XYSeriesCollection();
	protected static int counter = 1;
	
	protected static int generationsNeeded;
	
	protected static Plot chart;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Set parameters
		Test testParameters = setParameters();
		
		//Draw graph
		chart = new Plot(
				"Results",
				"Evolution of the " + testParameters.getFitnessFunctionName() + "(" + testParameters.getFitnessFunction().getNGenes() + ")" + " fitness function", 
				dataset,
				testParameters
			);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		
		//Launch simulations
		launchSeveralSimulations(
				testParameters,
				1 //nSimulations
		);

		
	}
	
	
	
	
	
	
	//Set test parameters
	public static Test setParameters() {
		
		//If true, automatically load the best parameters from FitnessFunction.java with evolutionMode and nScenarios defined
		boolean bestParameters = true;
		
		//Structure ANN
		int[] structure = {
				6, //Input layer
				6, //Hidden layer 1
				3  //Output layer
			};
		
		//Number of bits used to encode each weight
		int nBitsPerWeight = 8;
		
		//Allowed range for the weights [-rangeMax, rangeMax]
		double rangeMax = Math.sqrt(1.0 / 6.0);
		
		//Evolution mode: static or dynamic
		int evolutionMode = FlatlandFitnessFunction.DYNAMIC;
		
		//Number of scenarios per fitness evaluation
		int nScenarios = 1;
		
		//Calcul nGenes for later
		int nConnections = FlatlandFitnessFunction.countConnections(structure);
		int nGenes = nConnections * nBitsPerWeight;
		
		//Initialize a new test
		Test testParameters;
		
		if(bestParameters) {
			testParameters = FitnessFunction.bestParameters(evolutionMode, nScenarios);
		}
		else {
			//Initialize a new test
			testParameters = new Test(
				101, //nGenerations,
				50, //sizePop,
				0.015, //mutationRate, %% 0.0015
				0.95, //crossoverRate,
				Test.MIXING, //adultSelectionMode,	%% We need elitism
				Test.TOURNAMENT, //parentSelectionMode,
				0.3, //epsilon, decrease social pressure %% 0.25
				1, //pK, increase social pressure %% 0.1
				new FlatlandFitnessFunction(
						structure, //ANN structure
						nBitsPerWeight, //Encoding size
						rangeMax, //Allowed range for weights
						nGenes, //Number of genes in each genotype
						ANN.TANH, //Activation function used for each neuron
						evolutionMode, //Static or dynamic mode
					nScenarios //Number of scenarios per genotype
					)
			);
		}
		
		return testParameters;
		
	}
	
	
	
	
	
	
	
	//Start evolution simulation
	public static int launchSimulation(Test testParameters, int nSimulations) {
		
		//Prepare all the populations
		Population[] populations = new Population[testParameters.getMaxNumberOfGenerations()];
		
		//Initialize the first population for the test
		populations[0] = new Population(testParameters);
		
		//Best fitness
		int maxFitnessEverReached = populations[0].maxFitness();
		int rankMaxFitnessEverReached = 0; 
		
		//Draw graph
		chart.setVisible(true);
		XYSeries maxFitnesses;
		XYSeries averageFitnesses = new XYSeries("Average fitness");;
		XYSeries sdFitnesses = new XYSeries("Standard deviation in fitness");
		if (nSimulations == 1) {
			maxFitnesses = new XYSeries("Max fitness");
			dataset.addSeries(maxFitnesses);
			dataset.addSeries(averageFitnesses);
			dataset.addSeries(sdFitnesses);
		}
		else {
			maxFitnesses = new XYSeries("Max fitness (run " + counter + ")");
			dataset.addSeries(maxFitnesses);
			counter++;
		}
		
		//Launch evolution
		for (int i = 0 ; i < testParameters.getMaxNumberOfGenerations() - 1 ; i++) {
			
			//Logging routine
			new Log("Generation n'" + (i+1));
			Log.log("Size: " + populations[i].getPopulationSize());
			Log.log("Best fitness: " + populations[i].maxFitness());
			Log.log("Average fitness: " + populations[i].averageFitness());
			Log.log("Standard deviation: " + populations[i].standardDeviationFitness());
			Log.log("Best phenotype: ");
			Log.log(populations[i].bestGenotype()); //Log.log converts genotype into phenotype representation
			
			//Update max fitness ever reached
			if (populations[i].maxFitness() > maxFitnessEverReached) {
				maxFitnessEverReached = populations[i].maxFitness();
				rankMaxFitnessEverReached = i;
			}
			
			//Get next generation
			populations[i+1] = populations[i].nextGeneration(); 
			
			//Update graph data
			maxFitnesses.add(i, (double)populations[i].maxFitness());
			if (nSimulations == 1) {
				averageFitnesses.add(i, populations[i].averageFitness());
				sdFitnesses.add(i, populations[i].standardDeviationFitness());
			}
			
		}
		
		//Log recap
		new Log("Best solution found");
		Log.log("Fitness: " + maxFitnessEverReached);
		Log.log("Generation: " + rankMaxFitnessEverReached);
		Log.log("Phenotype: ");
		Log.log(populations[rankMaxFitnessEverReached].bestGenotype());
		
		//Results
		FlatlandFitnessFunction fff = ((FlatlandFitnessFunction)(testParameters.fitnessFunction));
		int[] bestPhenotype = populations[rankMaxFitnessEverReached].bestGenotype().develop().attributes();
		ANN resultAnn = fff.ann();
		double[] decodedVector = fff.decodeFromGray(bestPhenotype); //Weights of the ANN
		resultAnn.setWeights(decodedVector); //Update ANN with weights computed by EA
		Simulation[] currentSimulation = fff.savedSimulation();
		
		//Additional log
		displayANN(decodedVector, fff.structure());
		test(resultAnn, 100);
		
		//Visualize scenarios for best ANN
		new Window(currentSimulation, resultAnn);
		
		return maxFitnessEverReached;
		
	}
	
	
	
	
	
	//Launch multiple simulations
	public static void launchSeveralSimulations(Test testParameters, int nSimulations) {
		for (int i = 0 ; i < nSimulations ; i++) {
			generationsNeeded = 0;
			launchSimulation(testParameters, nSimulations);
		}
	}
	
	
	//Display ANN structure
	public static void displayANN(double[] weights, int[] structure) {
		new Log("Resulting ANN");
		int counter = 0;
		for (int i = 0 ; i < structure.length ; i++) {
			Log.log("Layer #" + i);
			for (int j = 0 ; j < structure[i] ; j++) {
				Log.log("   Node #" + j + ": " + weights[counter]);
				counter++;
			}
		}
		Log.log("\n\n");
	}
	
	
	//Test ANN on several simulations
	public static void test(ANN ann, int nSimulations) {
		int fitnessTest = 0;
		double nFoodEaten = 0;
		double nPoisonEaten = 0;
		for (int i = 0 ; i < nSimulations ; i++) {
			Simulation simTest = new Simulation();
			for (int j = 0 ; j < 60 ; j++) {
				int dir = ann.output(simTest.sensors()); //Compute next move given the sensors
				simTest.move(dir); //Move the agent
			}
			fitnessTest += (int) (100 * (double)(simTest.score()) / (double)(simTest.scoreMax()));
			nFoodEaten += (int) (100 * (double)(simTest.nFoodEaten()) / (double)(simTest.nTotalFood()));
			nPoisonEaten += (int) (100 * (double)(simTest.nPoisonEaten()) / (double)(simTest.nTotalPoison()));
		}
		new Log("RESULT " + nSimulations + " TESTS");
		Log.log("Average fitness: " + fitnessTest / nSimulations + "%");
		Log.log("Average food eaten: " + nFoodEaten / nSimulations + "%");
		Log.log("Average poison eaten: " + nPoisonEaten / nSimulations + "%");
	}
	
	
	//Getter
	public static int generationsNeeded() {
		return generationsNeeded;
	}
	
	//Setter
	public static void setGenerationsNeeded(int g) {
		generationsNeeded = g;
	}
	
	
}


