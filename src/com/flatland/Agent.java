package com.flatland;

import java.awt.Color;
import java.awt.Graphics;

import com.main.Log;
import com.problem.FlatlandFitnessFunction;

//Collective behaviour
public class Agent extends Cell {
	
	protected int x, y;
	protected int orientation;
	
	//Orientations
	public static final int NORTH = 0,
							WEST = 1,
							EAST = 2,
							SOUTH = 3;
	
	//Directions
	public static final int FRONT = 0,
							LEFT = 1,
							RIGHT = 2,
							STAY = 3;
	
	public Agent(int x0, int y0) {
		super(x0, y0);
		this.x = x0;
		this.y = y0;
		this.orientation = Agent.NORTH;
	}
	
	//Clone
	public Agent(Agent clonedAgent) {
		super(clonedAgent.x, clonedAgent.y);
		this.x = clonedAgent.x;
		this.y = clonedAgent.y;
		this.orientation = Agent.NORTH;
	}

	//Paint the agent
	public void draw(Graphics g) {
		
		//Reset
		super.draw(g);
		
		//Display agent
		int cellWidth = Flatland.mapWidth / FlatlandFitnessFunction.gridLength();
		int cellHeight = Flatland.mapHeight / FlatlandFitnessFunction.gridLength();
		g.setColor(Color.BLUE);
		g.fillOval(
			cellWidth * x + margin,
			cellHeight * y + margin, 
			cellWidth - 2 * margin, 
			cellHeight - 2 * margin
		);
		
		//Display eyes
		g.setColor(Color.YELLOW);
		int posEyeX1, posEyeY1, posEyeX2, posEyeY2;
		switch(orientation) {
		case Agent.NORTH:
			posEyeX1 = cellWidth * x + margin + (cellWidth - margin) / 3 - (cellWidth - 2 * margin) / 20;
			posEyeY1 = cellHeight * y + margin + (cellHeight - margin) / 6 - (cellHeight - 2 * margin) / 20;
			posEyeX2 = cellWidth * x + margin + 2 * (cellWidth - margin) / 3 - (cellWidth - 2 * margin) / 20;
			posEyeY2 = cellHeight * y + margin + (cellHeight - margin) / 6 - (cellHeight - 2 * margin) / 20;
			break;
		case Agent.WEST:
			posEyeX1 = cellWidth * x + margin + (cellWidth - margin) / 6 - (cellWidth - 2 * margin) / 20;
			posEyeY1 = cellHeight * y + margin + (cellHeight - margin) / 3 - (cellHeight - 2 * margin) / 20;
			posEyeX2 = cellWidth * x + margin + (cellWidth - margin) / 6 - (cellWidth - 2 * margin) / 20;
			posEyeY2 = cellHeight * y + margin + 2 * (cellHeight - margin) / 3 - (cellHeight - 2 * margin) / 20;
			break;
		case Agent.EAST:
			posEyeX1 = cellWidth * x + margin + 5 * (cellWidth - margin) / 6 - (cellWidth - 2 * margin) / 20;
			posEyeY1 = cellHeight * y + margin + (cellHeight - margin) / 3 - (cellHeight - 2 * margin) / 20;
			posEyeX2 = cellWidth * x + margin + 5 * (cellWidth - margin) / 6 - (cellWidth - 2 * margin) / 20;
			posEyeY2 = cellHeight * y + margin + 2 * (cellHeight - margin) / 3 - (cellHeight - 2 * margin) / 20;
			break;
		case Agent.SOUTH:
			posEyeX1 = cellWidth * x + margin + (cellWidth - margin) / 3 - (cellWidth - 2 * margin) / 20;
			posEyeY1 = cellHeight * y + margin + 5 * (cellHeight - margin) / 6 - (cellHeight - 2 * margin) / 20;
			posEyeX2 = cellWidth * x + margin + 2 * (cellWidth - margin) / 3 - (cellWidth - 2 * margin) / 20;
			posEyeY2 = cellHeight * y + margin + 5 * (cellHeight - margin) / 6 - (cellHeight - 2 * margin) / 20;
			break;
		default:
			throw new IllegalArgumentException("Eyes direction not known");
		}
		
		g.fillOval(
			posEyeX1,
			posEyeY1, 
			(cellWidth - 2 * margin) / 10, 
			(cellHeight - 2 * margin) / 10
		);
		g.fillOval(
			posEyeX2,
			posEyeY2, 
			(cellWidth - 2 * margin) / 10, 
			(cellHeight - 2 * margin) / 10
		);
		
	}
	
	
	
	//Setters
	public void setX(int x0) {
		this.x = x0;
	}
	public void setY(int y0) {
		this.y = y0;
	}
	public void setOrientation(int ori) {
		this.orientation = ori;
	}
	
	//Getters
	public int x() {
		return this.x;
	}
	public int y() {
		return this.y;
	}
	public int orientation() {
		return this.orientation;
	}
	
	
}
