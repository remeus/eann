package com.ea;

public class BinaryVector {
	
	int[] coordinates;
	int length = 0;
	
	public BinaryVector(int[] coord) {
		setLength(coord.length);
		setCoordinates(coord);
	}
	
	public BinaryVector(int lengthVector) {
		length = lengthVector;
		coordinates = new int[length];
		for (int i = 0 ; i < lengthVector ; i++) {
			coordinates[i] = randomCoordinate();
		}
	}
	

	
	public int randomCoordinate() {
		return (int) Math.round(Math.random());
	}
	
	
	//Mutation
	public void mutate(double rate) {
		for (int i = 0 ; i < length ; i++) {
			if (Math.random() <= rate) {
				coordinates[i] = 1 - coordinates[i]; //...Another technique would be to use randomCoordinate
			}
		}
	}
	
	//One-point crossover resulting in two children
	public BinaryVector crossover(BinaryVector secondParent, double rate, int crossoverPoint) {
		if (secondParent.length != length) {
			throw new IllegalArgumentException("Trying to perform a crossover between two vectors of different lengths");
		}
		else {
			BinaryVector result = new BinaryVector(length);
			if (Math.random() <= rate) { //Crossover //...Not the same random number for both children
				for (int i = 0 ; i < length ; i++) {
					if (i < crossoverPoint)
						result.setCoordinate(i, this.getCoordinates()[i]); //We keep
					else
						result.setCoordinate(i, secondParent.getCoordinates()[i]); //We switch
				}
			}
			else { //Simple copy
				for (int i = 0 ; i < length ; i++)
					result.setCoordinate(i, this.getCoordinates()[i]); //We keep all the genes
			}
			return result;
		}
	}
	
	
	
	//Getters
	
	public int[] getCoordinates() {
		return coordinates;
	}
	
	
	
	
	//Setters
	
	public void setLength(int size) {
		length = size;
	}
	
	public void setCoordinates(int[] coord) {
		coordinates = new int[length];
		for (int i = 0 ; i < length ; i++) {
			setCoordinate(i, coord[i]);
		}
	}
	
	public void setCoordinate(int index, int coord) {
		if (coord == 0 || coord == 1)
			coordinates[index] = coord;
		else {
			coordinates[index] = 0;
			System.out.println("Warning: a vector has been set up with the non binary value " + coord + ". The value has been automatically changed to 0.");
		}
	}

}
