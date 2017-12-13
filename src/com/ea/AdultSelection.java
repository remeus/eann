package com.ea;

import java.util.ArrayList;
import java.util.Arrays;

import com.main.Log;
import com.main.Test;

public class AdultSelection {

	protected Genotype[] parents;
	protected Genotype[] children;
	
	protected int nParents;
	protected int nChildren;
	
	protected int mode;
	
	protected static final double pRemainingParentsMixing = 0.05; //In mixing mode, p * length parents surviving
	
	//Create the children from the mating pool, using one-point crossover
	public AdultSelection(Genotype[] parentsGenotype) {
		
		parents = parentsGenotype;
		nParents = parents.length;
		
		mode = Population.testParameters.getAdultSelectionMode();
		
		ArrayList<Genotype> childrenGenotype = new ArrayList<Genotype>();
		Genotype[] resultingTwoChildren = new Genotype[2]; //One-point crossover with two children only
		ArrayList<Genotype> parentsList = new ArrayList<Genotype>(Arrays.asList(parents)); //We create a list of parents available
		if (mode == Test.OVER_PRODUCTION) { //In that case we double the number of parents
			parentsList.addAll(parentsList);
		}
		//For each couple of parents still available, we create the two children and remove the parents from the list
		while (parentsList.size() > 1) {
			int randomIndex = (int) (Math.random() * (parentsList.size() - 2) + 1);
			resultingTwoChildren = parentsList.get(0).crossover(parentsList.get(randomIndex));
			childrenGenotype.add(resultingTwoChildren[0]);
			childrenGenotype.add(resultingTwoChildren[1]);
			parentsList.remove(randomIndex);
			parentsList.remove(0); //Must be the last removed
		}
		if (parentsList.size() == 1) { //If the number of parents is not even we just copy the remaining as a children
			childrenGenotype.add(parentsList.get(0));
		}
		children = childrenGenotype.toArray(new Genotype[childrenGenotype.size()]);
		nChildren = children.length;
	}
	
	
	public Genotype[] replace() {
		Genotype[] newGeneration = new Genotype[nParents];
		switch (mode) {
			case Test.FULL:
				newGeneration = fullReplace();
				break;
			case Test.OVER_PRODUCTION:
				newGeneration = overProductionReplace();
				break;
			case Test.MIXING:
				newGeneration = mixingReplace();
				break;
			default:
				throw new IllegalArgumentException("Invalid adult selection mode");
		}
		return newGeneration;
	}
	
	
	//All the parents are replaced by all the children
	public Genotype[] fullReplace() {
		return children;
	}
	
	
	//All the parents are replaced by some of the children
	public Genotype[] overProductionReplace() {
		Genotype[] result = new Genotype[nParents];
		ArrayList<Genotype> availableChildren = new ArrayList<Genotype>(Arrays.asList(children));
		for (int i = 0 ; i < nParents ; i++) {
			int indexBest = Tournament.rankMaxFitness(availableChildren.toArray(new Genotype[availableChildren.size()]));
			result[i] = availableChildren.get(indexBest); //We only keep the best children //..Another solution: having another parent selection here
			availableChildren.remove(indexBest);
		}
		return result;
	}
	
	
	//Most of the parents are replaced by most of the children
	public Genotype[] mixingReplace() {
		Genotype[] result = new Genotype[nParents];
		int nParentsRemaining = (int)(nParents * pRemainingParentsMixing) + 1;
		ArrayList<Genotype> availableChildren = new ArrayList<Genotype>(Arrays.asList(children));
		ArrayList<Genotype> availableParents = new ArrayList<Genotype>(Arrays.asList(parents));
		for (int i = 0 ; i < nParentsRemaining ; i++) {
			int indexBest = Tournament.rankMaxFitness(availableParents.toArray(new Genotype[availableParents.size()]));
			result[i] = availableParents.get(indexBest); //We keep the best parents
			availableParents.remove(indexBest);
		}
		for (int i = nParentsRemaining ; i < nParents ; i++) {
			int indexBest = Tournament.rankMaxFitness(availableChildren.toArray(new Genotype[availableChildren.size()]));
			result[i] = availableChildren.get(indexBest); //We then fill with the best children
			availableChildren.remove(indexBest);
		}
		return result;
	}
	
}
