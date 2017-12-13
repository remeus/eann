package com.flatland;

import java.awt.Color;
import java.awt.Graphics;

import com.problem.FlatlandFitnessFunction;

//Collective behaviour
public class Poison extends Cell {
	
	protected int x, y;
	
	public Poison(int x0, int y0) {
		super(x0, y0);
		this.x = x0;
		this.y = y0;
	}
	
	//Clone
	public Poison(Poison clonedPoison) {
		super(clonedPoison.x, clonedPoison.y);
		this.x = clonedPoison.x;
		this.y = clonedPoison.y;
	}

	//Paint the agent
	public void draw(Graphics g) {
		
		//Reset
		super.draw(g);
		
		//Display food
		int cellWidth = Flatland.mapWidth / FlatlandFitnessFunction.gridLength();
		int cellHeight = Flatland.mapHeight / FlatlandFitnessFunction.gridLength();
		g.setColor(Color.decode("#cc3333"));
		g.fillOval(
			cellWidth * x + cellWidth / 4 + margin,
			cellHeight * y + cellHeight / 4 + margin, 
			cellWidth / 2 - 2 * margin, 
			cellHeight / 2 - 2 * margin
		);
		
	}
	
	
	
}
