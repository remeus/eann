package com.flatland;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ea.Population;
import com.problem.ANN;
import com.problem.FlatlandFitnessFunction;
import com.problem.Simulation;

public class Window extends JFrame {
	
	protected static int panelHeight = FlatlandFitnessFunction.gridLength() * 60; //Map height
	protected static int panelWidth = FlatlandFitnessFunction.gridLength() * 60; //Map width
	
	protected static int height = panelHeight + 28 + 30; //Full height
	protected static int width = panelWidth + 220; //Full width
	
	protected Flatland[] panel;
	protected Simulation[] simulation;
	
	protected Flatland[] savedPanel;
	protected Simulation[] savedSimulation;
	protected ANN ann;
	
	protected static int sleepLength;
	
	protected JButton startButton;
	protected static boolean pause = false;
	protected static boolean replay = false;
	protected static boolean newScenario = false;
	
	protected static JTabbedPane tabs;
	
	
	
	public Window(Simulation[] sim, ANN ann) {
		
		sleepLength = 500;
		start(sim, ann, 0);
				
	}
	
	
	public Window(Simulation[] sim, ANN ann, int sleep, int index) {
		
		sleepLength = sleep;
		start(sim, ann, index);
		
	}
	
	
	//Start
	public void start(Simulation[] sim, ANN ann, int indexTab) {
		
		pause = false;
		replay = false;
		newScenario = false;
		JButton startButton;
		
		//Prepare simulations
		simulation = new Simulation[sim.length];
		panel = new Flatland[sim.length];
		for (int i = 0 ; i < sim.length ; i++) {
			simulation[i] = new Simulation(sim[i]);
			panel[i] = new Flatland(simulation[i], ann);
		}
		this.ann = ann;
		
		//Settings
		this.setTitle("Flatland");
		this.setSize(Window.width, Window.height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		
		//Right menu
		Box box = Box.createVerticalBox();
		
		//Slider 1
		box.add(Box.createVerticalStrut(10));
		JLabel speedLabel = new JLabel("Speed");
		speedLabel.setAlignmentX(CENTER_ALIGNMENT);
		JSlider speedSlider = new JSlider();
		speedSlider.setBackground(Color.decode("#dddddd"));
		speedSlider.setForeground(Color.decode("#333333"));
		speedSlider.setMaximum(100);
		speedSlider.setMinimum(0);
		speedSlider.setValue((int) (Math.log(5000 / sleepLength) / 0.056));
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setMinorTickSpacing(10);
		speedSlider.setMajorTickSpacing(20);
		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent event) {
				int s = ((JSlider)event.getSource()).getValue();
				int sleepLength = (int) (5000 * Math.exp(-0.056 * s));
				Window.setSleepLength(sleepLength);
			}
		});
		box.add(speedLabel);
		box.add(speedSlider);
		box.setBackground(Color.GRAY);
		
		//Buttons "Start"
		box.add(Box.createVerticalStrut(50));
		startButton = new JButton("Pause");
		startButton.setBackground(Color.decode("#333333"));
		startButton.setForeground(Color.WHITE);
		startButton.setFocusPainted(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (startButton.getText() == "Pause") {
					startButton.setText("Start");
					startButton.setBackground(Color.decode("#33cc33"));
					pause = true;
				}
				else {
					startButton.setText("Pause");
					startButton.setBackground(Color.decode("#333333"));
					pause = false;
				}
			}
		});
		Box boxH = Box.createHorizontalBox();
		boxH.add(startButton);
		boxH.add(new JLabel("  "));
		
		//Buttons "Replay"
		box.add(Box.createVerticalStrut(50));
		JButton replayButton = new JButton("Replay");
		replayButton.setBackground(Color.decode("#3333dd"));
		replayButton.setForeground(Color.WHITE);
		replayButton.setFocusPainted(false);
		replayButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				replay = true;
			}
		});
		boxH.add(replayButton);
		box.add(boxH);
		
		//Parameters
		FlatlandFitnessFunction fff = ((FlatlandFitnessFunction)(Population.testParameters().getFitnessFunction()));
		box.add(Box.createVerticalStrut(50));
		JLabel parameters = new JLabel("Parameters");
		parameters.setAlignmentX(CENTER_ALIGNMENT);
		parameters.setForeground(Color.decode("#000000"));
		parameters.setFont(new Font(speedLabel.getFont().getName(), Font.BOLD, 15));
		box.add(parameters);
		box.add(Box.createVerticalStrut(10));
		//Structure ANN
		String annStructure = "(";
		for (int i = 0 ; i < fff.structure().length ; i++) {
			annStructure += fff.structure()[i] + ", ";
		}
		annStructure = annStructure.substring(0, annStructure.length() - 2);
		annStructure += ")";
		JLabel parameter1 = new JLabel("ANN structure: " + annStructure);
		parameter1.setAlignmentX(CENTER_ALIGNMENT);
		parameter1.setForeground(Color.decode("#333333"));
		box.add(parameter1);
		box.add(Box.createVerticalStrut(5));
		//Bits per weight
		JLabel parameter2 = new JLabel("Bits per weight: " + fff.nBitsPerWeight());
		parameter2.setAlignmentX(CENTER_ALIGNMENT);
		parameter2.setForeground(Color.decode("#333333"));
		box.add(parameter2);
		box.add(Box.createVerticalStrut(5));
		//RangeMax
		double r = Math.round(fff.rangeMax() * 1000) / 1000.0;
		JLabel parameter3 = new JLabel("Range: [" + (-r) + ", " + r + "]");
		parameter3.setAlignmentX(CENTER_ALIGNMENT);
		parameter3.setForeground(Color.decode("#333333"));
		box.add(parameter3);
		box.add(Box.createVerticalStrut(5));
		//Activation function
		String activation;
		switch (fff.activationFunctionMode()) {
			case ANN.STEP:
				activation = "Step";
				break;
			case ANN.LOGISTIC:
				activation = "Logistic";
				break;
			case ANN.TANH:
				activation = "Tanh";
				break;
			default:
				throw new IllegalArgumentException("Activation function unknown");
		}
		JLabel parameter4 = new JLabel("Activation: " + activation);
		parameter4.setAlignmentX(CENTER_ALIGNMENT);
		parameter4.setForeground(Color.decode("#333333"));
		box.add(parameter4);
		box.add(Box.createVerticalStrut(5));
		//RangeMax
		String evolutionMode;
		switch (fff.evolutionMode()) {
			case FlatlandFitnessFunction.STATIC:
				evolutionMode = "Static";
				break;
			case FlatlandFitnessFunction.DYNAMIC:
				evolutionMode = "Dynamic";
				break;
			default:
				throw new IllegalArgumentException("Evolution mode unknown");
		}
		JLabel parameter5 = new JLabel("Evolution mode: " + evolutionMode);
		parameter5.setAlignmentX(CENTER_ALIGNMENT);
		parameter5.setForeground(Color.decode("#333333"));
		box.add(parameter5);
		
		//Buttons "New scenario"
		box.add(Box.createVerticalStrut(50));
		JButton newScenarioButton = new JButton("New scenario");
		newScenarioButton.setBackground(Color.decode("#d65e00"));
		newScenarioButton.setForeground(Color.WHITE);
		newScenarioButton.setFocusPainted(false);
		newScenarioButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				newScenario = true;
			}
		});
		Box boxH2 = Box.createHorizontalBox();
		boxH2.add(newScenarioButton);
		box.add(boxH2);
		
		
		//Display window
		this.getContentPane().add(box, BorderLayout.EAST);
		this.getContentPane().setBackground(Color.decode("#dddddd")); 
		
		
		
		//Tabs
		tabs = new JTabbedPane();
		for (int i = 0 ; i < simulation.length ; i++) {
			this.panel[i].setBackground(Color.decode("#333333"));
			tabs.addTab("Simulation " + (i + 1), panel[i]);
		}
		tabs.setSelectedIndex(indexTab);
		this.getContentPane().add(tabs);
		this.setVisible(true);
		
		//Save for replay
		savedSimulation = new Simulation[sim.length];
		savedPanel = new Flatland[sim.length];
		for (int i = 0 ; i < sim.length ; i++) {
			savedSimulation[i] = new Simulation(simulation[i]);
			savedPanel[i] = new Flatland(panel[i]);
		}
		
		animation();
		
		
	}
	
	
	//Animation
	public void animation() {
		
		int nTabs = simulation.length;
		int currentTimestep[] = new int[nTabs];
		for (int i = 0 ; i < nTabs ; i++) {
			currentTimestep[i] = FlatlandFitnessFunction.timesteps();
		}
		
		for(;;) {
			
			int currentTab = tabs.getSelectedIndex();
			
			if (replay) {
				replay = false;
				setVisible(false);
				dispose();
				//start(savedSimulation, ann, currentTab);
				new Window(savedSimulation, ann, sleepLength, currentTab);
			}
			
			if (newScenario) {
				newScenario = false;
				setVisible(false);
				dispose();
				Simulation[] newSimulations = new Simulation[simulation.length];
				for (int i = 0 ; i < newSimulations.length ; i++) {
					if (i == currentTab)
						newSimulations[currentTab] = new Simulation();
					else
						newSimulations[i] = new Simulation(savedSimulation[i]);
				}
				//start(newSimulations, ann);
				new Window(newSimulations, ann, sleepLength, currentTab);
			}
				
			if (!Window.pause) {
				
				if (currentTimestep[currentTab] > 0) {
					currentTimestep[currentTab]--;
					try {
				        Thread.sleep(Window.sleepLength());
				    } catch (InterruptedException e) {
				        e.printStackTrace();
				    }
					int dir = panel[currentTab].computedAnn.output(panel[currentTab].runningSimulation.sensors()); //Compute next move given the sensors
					panel[currentTab].runningSimulation.move(dir); //Move the agent
					panel[currentTab].repaint();
				}
				else {
					//Simulation done
					try {
				        Thread.sleep(100);
				    } catch (InterruptedException e) {
				        e.printStackTrace();
				    }
				}
				
			}
			
			else {
				
				try {
			        Thread.sleep(100);
			    } catch (InterruptedException e) {
			        e.printStackTrace();
			    }
				
			}
			
		}
		
	}
	
	
	//Getters
	
	public static int height() {
		return Window.height;
	}
	
	public static int width() {
		return Window.width;
	}
	
	public static int panelHeight() {
		return Window.panelHeight;
	}
	
	public static int panelWidth() {
		return Window.panelWidth;
	}
	
	public static int sleepLength() {
		return Window.sleepLength;
	}
	
	//Setters
	
	public static void setPanelHeight(int h) {
		Window.panelHeight = h;
	}
	
	public static void setPanelWidth(int w) {
		Window.panelWidth = w;
	}
	
	public static void setSleepLength(int s) {
		Window.sleepLength = s;
	}
	
	

}
