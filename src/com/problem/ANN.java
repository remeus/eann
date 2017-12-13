package com.problem;

import com.flatland.Agent;
import com.main.Log;

public class ANN {
	
	protected int[] structure;
	protected int nNeurons;
	protected double[] weights;
	protected double bias;
	protected int activationFunctionMode;
	
	protected double threshold = 0; // No threshold needed
	
	protected Node[][] nodes; //Neural network: nodes[nLayers][nNodesForThisLayer]
	
	public static final int STEP = 0,
							LOGISTIC = 1,
							TANH = 2;
	
	public ANN(int[] structure, int nNeurons, double bias, int activationFunctionMode) {
		this.structure = structure;
		this.nNeurons = nNeurons;
		this.weights = new double[nNeurons];
		this.bias = bias;
		this.activationFunctionMode = activationFunctionMode;
		//Initialize network
		this.nodes = new Node[structure.length][];
		for (int i = 0 ; i < structure.length ; i++) {
			this.nodes[i] = new Node[structure[i]]; //Layer
			for (int j = 0 ; j < structure[i] ; j++) {
				this.nodes[i][j] = new Node(activationFunctionMode); //Neuron
			}
		}
	}

	
	//Output of the ANN
	public int output(double[] input) {
		int counter = 0;
		if (input.length != nodes[0].length)
			throw new IllegalArgumentException("Error in the input of the ANN");
		for (int i = 1 ; i < nodes.length ; i++) { //Layer (except input layer)
			for (int j = 0 ; j < nodes[i].length ; j++) { //Neuron
				//Set inputs arriving to the neuron
				if (i == 1) {
					nodes[1][j].setInputs(input);
				}
				else {
					double[] outputs = new double[nodes[i-1].length];
					for (int k = 0 ; k < outputs.length ; k++) {
						outputs[k] = nodes[i-1][k].output();
					}
					nodes[i][j].setInputs(outputs);
				}
				//Set weights arriving to the neuron
				double[] nodeWeights = new double[nodes[i-1].length];
				for (int k = 0 ; k < nodeWeights.length ; k++) {
					nodeWeights[k] = weights[counter]; //List of weights starts with every connections to 1st neuron of 2nd layer, then every connections to 2nd neuron of 2nd layer... until last neuron of last layer
					counter++;
				}
				nodes[i][j].setWeights(nodeWeights);
			}
		}
		
		double probaFront = nodes[nodes.length - 1][Agent.FRONT].output(),
				probaLeft = nodes[nodes.length - 1][Agent.LEFT].output(),
				probaRight = nodes[nodes.length - 1][Agent.RIGHT].output();
		
		
		if (probaFront >= probaLeft && probaFront >= probaRight)
			return Agent.FRONT;
		else if (probaLeft >= probaFront && probaLeft >= probaRight)
			return Agent.LEFT;
		else
			return Agent.RIGHT;
		
	}
	
	
	//Activation function
	public static double activationFunction(double x, int mode) {
		switch(mode) {
			case ANN.STEP:
				if (x >= 0)
					return 1;
				else
					return 0;
			case ANN.LOGISTIC:
				return 1 / (1 + Math.exp(-x));
			case ANN.TANH:
				return Math.tanh(x);
			default:
				throw new IllegalArgumentException("This activation function is not known.");
		}
	}
	
	
	//Getters
	public double[] weights() {
		return this.weights;
	}
	
	
	//Setters
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	
}
