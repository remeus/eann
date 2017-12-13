package com.flatland;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.problem.FlatlandFitnessFunction;

//Collective behaviour
public class Cell extends JPanel {
	
	protected int x, y;
	
	protected static int margin = 2;
	
	public Cell(int x0, int y0) {
		this.x = x0;
		this.y = y0;
	}
	
	//Clone
	public Cell(Cell clonedCell) {
		this.x = clonedCell.x;
		this.y = clonedCell.y;
	}

	//Paint the map and components
	public void draw(Graphics g) {
		
		//Display obstacles
		g.setColor(Color.GRAY);
		int cellWidth = Flatland.mapWidth / FlatlandFitnessFunction.gridLength();
		int cellHeight = Flatland.mapHeight / FlatlandFitnessFunction.gridLength();
		g.fillRect(
				cellWidth * x + margin,
				cellHeight * y + margin,
				cellWidth - 2 * margin,
				cellHeight - 2 * margin
			);
		
		
	}
	
	

	
}
