package com.flatland;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.problem.ANN;
import com.problem.FlatlandFitnessFunction;
import com.problem.Simulation;

public class Flatland extends JPanel {
	
	protected Simulation runningSimulation;
	protected ANN computedAnn;
	
	protected static int resultBoxHeight = 35;
	
	protected static int mapWidth = Window.panelWidth,
						mapHeight = Window.panelHeight - resultBoxHeight;
	
	public Flatland(Simulation sim, ANN ann) {
		
		//Simulation
		runningSimulation = sim;
		
		//ANN
		computedAnn = ann;
		
	}
	
	//Clone
	public Flatland(Flatland clonedFlatland) {
		
		this.runningSimulation = clonedFlatland.runningSimulation;
		this.computedAnn = clonedFlatland.computedAnn;
		
	}
	
	//Paint the flatland
	public void paint(Graphics g) {
		
		//Reset
		super.paint(g);
		
		//Display background
		g.setColor(Color.decode("#333333"));
		g.fillRect(
			0,
			0,
			Window.panelWidth,
			Window.panelHeight
		);
		
		//Display cells
		for (int i = 0 ; i < runningSimulation.dim() ; i++) {
			for (int j = 0 ; j < runningSimulation.dim() ; j++) {
				(new Cell(i, j)).draw(g);
				runningSimulation.map()[i][j].draw(g);
			}
		}
		
		//Display agent
		runningSimulation.agent().draw(g);
		
		//Display box score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.PLAIN, 20));
		g.drawString(
			"Score: " + runningSimulation.score() + "/" + runningSimulation.scoreMax() 
			+ "          " + "Food: " + runningSimulation.nFoodEaten()
			+ "          " + "Poison: " + runningSimulation.nPoisonEaten()
			+ "          " + "Step: " + runningSimulation.step() + "/" + FlatlandFitnessFunction.timesteps(), 
			10, 
			mapHeight + 18
		);
	}

	
	// For now, random output for the ANN
	public int output() {
		return (int) (Math.round(Math.random() * 2));
	}
	
	
}
