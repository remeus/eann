package com.problem;

import com.main.Main;

//Score got by the ANN for the Flatland Game
public class FlatlandFitnessFunction extends FitnessFunction {

	protected int nBitsPerWeight; //Available bits to encode a given weight
	protected double rangeMax; //Max range for the weights, so that every weight belongs to [-rangeMax;rangeMax]
	
	protected int nGenes;
	
	protected int[] structure;
	
	// Fixed problem data
	protected static double foodRate = 0.3;
	protected static double poisonRemainingRate = 0.3;
	protected static int gridLength = 10;
	protected static int timesteps = 60;
	
	protected ANN ann;
	
	protected int evolutionMode;
	protected int activationFunctionMode;
	
	public static final int STATIC = 0,
							DYNAMIC = 1;
	
	protected int nScenarios;
	
	protected Simulation[] savedSimulation;
	
	protected static int nCurrentGeneration = 0;
	
	
	public FlatlandFitnessFunction(int[] structure, int nBitsPerWeight, double rangeMax, int nGenes, int activationFunction, int evolutionMode, int nScenarios) {
		super(nGenes);
		this.nBitsPerWeight = nBitsPerWeight;
		this.rangeMax = rangeMax;
		this.nGenes = nGenes;
		this.structure = structure;
		this.evolutionMode = evolutionMode;
		this.activationFunctionMode = activationFunction;
		ann = new ANN(structure, nGenes / nBitsPerWeight, 0, activationFunction);
		this.nScenarios = nScenarios;
		this.savedSimulation = new Simulation[nScenarios];
		for (int i = 0 ; i < nScenarios ; i++)
			this.savedSimulation[i] = new Simulation();
	}
	
	
	//We convert the weights and evaluate the fitness
	public int fitness(int[] vector) {
		
		int fitness = 0;
		
		double[] decodedVector = decodeFromGray(vector); //Weights of the ANN
		ann.setWeights(decodedVector); //Update ANN with weights computed by EA
		
		//Run a full game
		Simulation[] newSimulation = new Simulation[nScenarios];
		for (int i = 0 ; i < nScenarios ; i++) {
			//STATIC mode
			if (evolutionMode == STATIC) { 
				newSimulation[i] = new Simulation(savedSimulation[i]);//Always keep the same simulation
			}
			//DYNAMIC mode
			else { 
				if (nCurrentGeneration == Main.generationsNeeded()) //Still at the same generation
					newSimulation[i] = new Simulation(savedSimulation[i]); //Just keep the same generation
				else if (nCurrentGeneration == Main.generationsNeeded() - 1) { //New generation
					savedSimulation[i] = new Simulation(); //Initialize a new simulation and save it
					newSimulation[i] = new Simulation(savedSimulation[i]); //Use this new simulation
					nCurrentGeneration = Main.generationsNeeded();
				}
				else
					throw new IllegalArgumentException("Problem with the counting of generations");
			}
			//Run simulation
			for (int j = 0 ; j < timesteps ; j++) {
				int dir = ann.output(newSimulation[i].sensors()); //Compute next move given the sensors
				newSimulation[i].move(dir); //Move the agent
			}
			//Update fitness
			fitness += (int) (100 * ((double)(newSimulation[i].score()) / (double)(newSimulation[i].scoreMax()))); //Return the score of the game
		}

		return fitness / nScenarios; 
		
	}
	
	
	//Convert a Gray binary vector into an integer vector. For instance [010,111] -> [3, 6]
	public double[] decodeFromGray(int[] sequence) {
		int lengthDecodedSequence = (int) ((double)sequence.length / nBitsPerWeight); //Number of weights i.e. connections between 2 neurons
		double[] decodedSequence = new double[lengthDecodedSequence]; //Integer vector
		int[] decodedFromGray = new int[sequence.length]; //Simple binary vector
		for (int i = 0 ; i < lengthDecodedSequence ; i++) {
			String binaryString = new String(""); //To store binary number
			for (int j = 0 ; j < nBitsPerWeight ; j++) {
				if (j == 0) //Gray decoding
					decodedFromGray[i * nBitsPerWeight + j] = sequence[i * nBitsPerWeight + j];
				else
					decodedFromGray[i * nBitsPerWeight + j] = decodedFromGray[i * nBitsPerWeight + j - 1] ^ sequence[i * nBitsPerWeight + j];
				binaryString += decodedFromGray[i * nBitsPerWeight + j];
			}
			decodedSequence[i] = Integer.parseInt(binaryString, 2); //From simple binary to integer
			decodedSequence[i] = decodedSequence[i] * (2 * rangeMax) / (Math.pow(2, (double) nBitsPerWeight)) - rangeMax; //Adjust to [-rangeMax ; rangeMax]
		}
		return decodedSequence;
	}
	
	
	//Sum elements array
	public static int countConnections(int[] array) {
		int nConnections = 0;
		if (array.length > 1) {
			for (int i = 1 ; i < array.length ; i++)
				nConnections += array[i] * array[i-1];
		}
		return nConnections;
	}
	
	
	//Getters
	public static int gridLength() {
		return gridLength;
	}
	public static double foodRate() {
		return foodRate;
	}
	public static double poisonRemainingRate() {
		return poisonRemainingRate;
	}
	public Simulation[] savedSimulation() {
		return savedSimulation;
	}
	public ANN ann() {
		return ann;
	}
	public static int timesteps() {
		return timesteps;
	}
	public int[] structure() {
		return structure;
	}
	public int nBitsPerWeight() {
		return nBitsPerWeight;
	}
	public double rangeMax() {
		return rangeMax;
	}
	public int activationFunctionMode() {
		return activationFunctionMode;
	}
	public int evolutionMode() {
		return evolutionMode;
	}
	
	//Setters
	public static void setNCurrentGeneration(int n) {
		nCurrentGeneration = n;
	}
}
