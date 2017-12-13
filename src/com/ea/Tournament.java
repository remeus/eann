package com.ea;

import java.util.ArrayList;
import java.util.Arrays;

public class Tournament extends ParentSelection {
	
	
	public Tournament(Genotype[] population) {
		
		super(population);
		selectCompetitors(population);
		
	}
	
	
	//Initialize the mating pool
	public void selectCompetitors(Genotype[] pop) {
		for (int i = 0 ; i < sizeMatingPool ; i++) {
			if (Math.random() <= Population.testParameters.getEpsilon()) {
				matingPool[i] = pop[(int)(Math.random() * (pop.length - 1))];
			}
			else {
				ArrayList<Genotype> competitors = new ArrayList<Genotype>(Arrays.asList(pop));
				cut(competitors, (int) Population.testParameters.getPK() * pop.length);
				int rankMaxFitness = rankMaxFitness(competitors);
				matingPool[i] = competitors.get(rankMaxFitness);
			}
		}
	}
	
	//Keep some elements in an arrayList randomly
	public void cut(ArrayList<Genotype> list, int n) {
		for (int i = 0 ; i < list.size() - n ; i++) {
			list.remove((int) Math.random() * list.size()); 
		}
	}
	
	
	//Shuffle an array of Genotypes randomly
	public static Genotype[] shuffle(Genotype[] pop) {
		int length = pop.length;
		Genotype[] shuffled = new Genotype[length];
		ArrayList<Integer> availableIndexes = new ArrayList<Integer>();
		for (int i = 0 ; i < length ; i++) {
			availableIndexes.add(i);
		}
		int nNonUsedIndexes = length;
		for (int i = 0 ; i < length ; i++) {
			int randomIndex = (int) (Math.random() * (nNonUsedIndexes - 1));
			int newIndex = availableIndexes.get(randomIndex);
			shuffled[newIndex] = pop[i];
			availableIndexes.remove(randomIndex);		
			nNonUsedIndexes -= 1;
		}
		return shuffled;
	}
	
	//Return the rank of the max fitness
	public static int rankMaxFitness(ArrayList<Genotype> pop) {
		int rank = 0;
		int maxFitness = pop.get(0).getFitness();
		int currentFitness = 0;
		for (int i = 0 ; i < pop.size(); i++) {
			currentFitness = pop.get(i).getFitness();
			if (currentFitness > maxFitness) {
				rank = i;
				maxFitness = currentFitness;
			}
		}
		return rank;
	}
	
	//Return the rank of the max fitness
	public static int rankMaxFitness(Genotype[] pop) {
		int rank = 0;
		int maxFitness = pop[0].getFitness();
		int currentFitness = 0;
		for (int i = 0 ; i < pop.length; i++) {
			currentFitness = pop[i].getFitness();
			if (currentFitness > maxFitness) {
				rank = i;
				maxFitness = currentFitness;
			}
		}
		return rank;
	}
	
	
	
	
}
