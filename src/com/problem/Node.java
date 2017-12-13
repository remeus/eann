package com.problem;

public class Node {
	
	protected double[] inputs, weights; //Arriving inputs and corresponding weights
	protected double bias; //Arriving bias
	protected int activationFunctionMode; //Node activation function mode
	
	
	public Node(int activationFunctionMode) {
		this.activationFunctionMode = activationFunctionMode;
		this.bias = 0; // No bias needed here
	}
	
	public double output() {
		double potential = bias;
		for (int i = 0 ; i < inputs.length ; i++)
			potential += inputs[i] * weights[i];
		return ANN.activationFunction(potential, activationFunctionMode);
	}
	
	
	//Setters
	public void setInputs(double[] inputs) {
		this.inputs = inputs;
	}
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
}
