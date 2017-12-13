package com.flatland;

import java.awt.Color;
import java.awt.Graphics;

import com.problem.FlatlandFitnessFunction;

//Collective behaviour
public class Food extends Cell {
	
	protected int x, y;
	
	public Food(int x0, int y0) {
		super(x0, y0);
		this.x = x0;
		this.y = y0;
	}
	
	//Clone
	public Food(Food clonedFood) {
		super(clonedFood.x, clonedFood.y);
		this.x = clonedFood.x;
		this.y = clonedFood.y;
	}

	//Paint the agent
	public void draw(Graphics g) {
		
		//Reset
		super.draw(g);
		
		//Display food
		int cellWidth = Flatland.mapWidth / FlatlandFitnessFunction.gridLength();
		int cellHeight = Flatland.mapHeight / FlatlandFitnessFunction.gridLength();
		g.setColor(Color.decode("#33cc33"));
		g.fillOval(
			cellWidth * x + cellWidth / 4 + margin,
			cellHeight * y + cellHeight / 4 + margin, 
			cellWidth / 2 - 2 * margin, 
			cellHeight / 2 - 2 * margin
		);
		
	}
	
	
}
