package com.problem;

import com.flatland.Agent;
import com.flatland.Cell;
import com.flatland.Food;
import com.flatland.Poison;

public class Simulation {
	
	protected final int dim = FlatlandFitnessFunction.gridLength();
	
	protected Agent agent;
	protected Cell[][] map;
	
	protected int nFoodEaten = 0,
				nPoisonEaten = 0,
				step = 0;
	
	protected int nTotalFood = 0,
				nTotalPoison = 0;
	
	// Launch a simulation of the game
	public Simulation() {
	
		//Map
		map = new Cell[dim][dim];
		
		//Food and Poison
		for (int i = 0 ; i < dim ; i++) {
			for (int j = 0 ; j < dim ; j++) {
				if (Math.random() < FlatlandFitnessFunction.foodRate()) {
					map[i][j] = new Food(i, j);
					nTotalFood++;
				}
				else if (Math.random() < FlatlandFitnessFunction.poisonRemainingRate()) {
					map[i][j] = new Poison(i, j);
					nTotalPoison++;
				}
				else
					map[i][j] = new Cell(i, j);
			}
		}
		
		//Agent
		int x = (int) (Math.random() * dim);
		int y = (int) (Math.random() * dim);
		agent = new Agent(x, y);
		
	}
	
	//Clone
	public Simulation(Simulation clonedSimulation) {
	
		//Map
		map = new Cell[clonedSimulation.dim][clonedSimulation.dim];
		
		//Food and Poison
		for (int i = 0 ; i < clonedSimulation.dim ; i++) {
			for (int j = 0 ; j < clonedSimulation.dim ; j++) {
				if (clonedSimulation.map[i][j] instanceof Food)
					map[i][j] = new Food((Food)clonedSimulation.map[i][j]);
				else if (clonedSimulation.map[i][j] instanceof Poison)
					map[i][j] = new Poison((Poison)clonedSimulation.map[i][j]);
				else
					map[i][j] = new Cell(clonedSimulation.map[i][j]);
			}
		}
		
		nTotalFood = clonedSimulation.nTotalFood;
		nTotalPoison = clonedSimulation.nTotalPoison;
		
		//Agent
		agent = new Agent(clonedSimulation.agent);
		
	}
	
	
	public void move(int dir) {
		
		//Log.log("Move agent: " + dir);
		
		int newX = agent.x(), newY = agent.y(), newOrientation = agent.orientation();
		
		//Prepare move
		if (dir == Agent.FRONT && agent.orientation() == Agent.NORTH
				|| dir == Agent.RIGHT && agent.orientation() == Agent.WEST
				|| dir == Agent.LEFT && agent.orientation() == Agent.EAST) {
			newY = agent.y() - 1;
			newOrientation = Agent.NORTH;
		}
		else if (dir == Agent.FRONT && agent.orientation() == Agent.SOUTH
				|| dir == Agent.RIGHT && agent.orientation() == Agent.EAST
				|| dir == Agent.LEFT && agent.orientation() == Agent.WEST) {
			newY = agent.y() + 1;
			newOrientation = Agent.SOUTH;
		}
		else if (dir == Agent.FRONT && agent.orientation() == Agent.WEST
				|| dir == Agent.RIGHT && agent.orientation() == Agent.SOUTH
				|| dir == Agent.LEFT && agent.orientation() == Agent.NORTH) {
			newX = agent.x() - 1;
			newOrientation = Agent.WEST;
		}
		else if (dir == Agent.FRONT && agent.orientation() == Agent.EAST
				|| dir == Agent.RIGHT && agent.orientation() == Agent.NORTH
				|| dir == Agent.LEFT && agent.orientation() == Agent.SOUTH) {
			newX = agent.x() + 1;
			newOrientation = Agent.EAST;
		}
		else
			throw new IllegalArgumentException("Trying to move the agent in a wrong direction");
		
		//Wrap world
		if (newY < 0)
			newY = FlatlandFitnessFunction.gridLength() - 1;
		else if (newY >= FlatlandFitnessFunction.gridLength())
			newY = 0;
		if (newX < 0)
			newX = FlatlandFitnessFunction.gridLength() - 1;
		else if (newX >= FlatlandFitnessFunction.gridLength())
			newX = 0;
		
		//Update score
		if (map[newX][newY] instanceof Food)
			nFoodEaten++;
		else if (map[newX][newY] instanceof Poison)
			nPoisonEaten++;
		
		//Delete possible food or poison
		map[newX][newY] = new Cell(newX, newY);
		
		//Move agent
		agent.setX(newX);
		agent.setY(newY);
		step++;
		
		//Update direction
		agent.setOrientation(newOrientation);
		
	}
	
	
	//Return sensors array [FRONT == Food, LEFT = Food, RIGHT = Food, FRONT = Posion, LEFT = Poison, RIGHT = Poison]
	public double[] sensors() {
		
		Cell[] neighbours = new Cell[3];
		
		Cell[] allNeighbours = new Cell[4];
		
		if (agent.y() > 0)
			allNeighbours[Agent.NORTH] = map[agent.x()][agent.y() - 1];
		else
			allNeighbours[Agent.NORTH] = map[agent.x()][dim - 1];
		
		if (agent.x() > 0)
			allNeighbours[Agent.WEST] = map[agent.x() - 1][agent.y()];
		else
			allNeighbours[Agent.WEST] = map[dim - 1][agent.y()];
		
		if (agent.x() < dim - 1)
			allNeighbours[Agent.EAST] = map[agent.x() + 1][agent.y()];
		else
			allNeighbours[Agent.EAST] = map[0][agent.y()];
		
		if (agent.y() < dim - 1)
			allNeighbours[Agent.SOUTH] = map[agent.x()][agent.y() + 1];
		else
			allNeighbours[Agent.SOUTH] = map[agent.x()][0];
		
		switch (agent.orientation()) {
			case Agent.NORTH:
				neighbours[Agent.FRONT] = allNeighbours[Agent.NORTH];
				neighbours[Agent.LEFT] = allNeighbours[Agent.WEST];
				neighbours[Agent.RIGHT] = allNeighbours[Agent.EAST];
				break;
			case Agent.WEST:
				neighbours[Agent.FRONT] = allNeighbours[Agent.WEST];
				neighbours[Agent.LEFT] = allNeighbours[Agent.SOUTH];
				neighbours[Agent.RIGHT] = allNeighbours[Agent.NORTH];
				break;
			case Agent.EAST:
				neighbours[Agent.FRONT] = allNeighbours[Agent.EAST];
				neighbours[Agent.LEFT] = allNeighbours[Agent.NORTH];
				neighbours[Agent.RIGHT] = allNeighbours[Agent.SOUTH];
				break;
			case Agent.SOUTH:
				neighbours[Agent.FRONT] = allNeighbours[Agent.SOUTH];
				neighbours[Agent.LEFT] = allNeighbours[Agent.EAST];
				neighbours[Agent.RIGHT] = allNeighbours[Agent.WEST];
				break;
			default:
				throw new IllegalArgumentException("Trying to move the agent in a wrong direction");
		}
		
		return convertToInput(neighbours);
	}
	
	
	//Convert neighbour cells into ANN input
	public double[] convertToInput(Cell[] neighbours) {
		double[] result = new double[6];
		for (int i = 0 ; i < 3 ; i++) {
			//Food
			if (neighbours[i] instanceof Food)
				result[i] = 1;
			else
				result[i] = 0;
			//Poison
			if (neighbours[i] instanceof Poison)
				result[i + 3] = 1;
			else
				result[i + 3] = 0;
		}
		return result;
	}
	
	
	//Return score
	public int score() {
		return 2 * nFoodEaten - nPoisonEaten;
	}
	
	public int scoreMax() {
		return 2 * nTotalFood;
	}
	
	
	
	//Getters
	public Cell[][] map() {
		return map;
	}
	public Agent agent() {
		return agent;
	}
	public int dim() {
		return dim;
	}
	public int nFoodEaten() {
		return nFoodEaten;
	}
	public int nPoisonEaten() {
		return nPoisonEaten;
	}
	public int nTotalFood() {
		return nTotalFood;
	}
	public int nTotalPoison() {
		return nTotalPoison;
	}
	public int step() {
		return step;
	}
	
	
}
