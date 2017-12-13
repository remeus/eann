package com.main;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;

public class Plot extends ApplicationFrame {
	
   public static int nSimulations = 0;
   
   public JFreeChart chart;

   public Plot(String applicationTitle, String chartTitle, XYDataset data, Test testParameters) {
      //Create graph
	  super(applicationTitle);
      chart = ChartFactory.createXYLineChart(
         chartTitle,
         "Generations",
         "Fitness",
         data,
         PlotOrientation.VERTICAL,
         true,true,false);
         
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
      //Display graph
      setContentPane(chartPanel);
      //Add legend
      TextTitle legendText = new TextTitle(
    		  "Generations: " + testParameters.getMaxNumberOfGenerations() + " | "
      		+ "Population size: " + testParameters.getMaxPopulationSize() + "\n"
      		+ "Mutation rate: " + testParameters.getMutationRate() + " | "
      		+ "Crossover rate: " + testParameters.getCrossoverRate() + "\n"
      		+ "Adult selection: " + testParameters.getAdultSelectionModeName() + " | "
      		+ "Parent selection: " + testParameters.getParentSelectionModeName() + "\n"
      	);
      legendText.setPosition(RectangleEdge.TOP);
      chart.addSubtitle(legendText);
 
   }

   
   public void windowClosing(final WindowEvent evt){
	   if(evt.getWindow() == this) {
		   //Save image
		   nSimulations++;
		   File lineChartFile = new File("res/Simulation_" + Plot.nSimulations + ".png"); 
		   try {
			   ChartUtilities.saveChartAsPNG(lineChartFile, chart, 800, 600);
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   dispose();
	   }
	   
   }

   
   
   
}