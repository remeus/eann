package com.main;

import com.ea.Genotype;

//Display results and test
public class Log {

	protected static int nLogs = 0;
	
	public Log(String msg) {
		nLogs++;
		System.out.println("\n\n--- " + msg + " ---"); //Title
	}
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public static void log(int msg) {
		System.out.println(msg);
	}
	
	public static void log(int[] msg) {
		String ch = "(";
		for (int i = 0 ; i < msg.length - 1 ; i++) {
			ch += msg[i] + ", ";
		}
		ch += msg[msg.length - 1] + ")";
		System.out.println(ch);
	}
	
	public static void log(Genotype genotype) {
		int[] msg = new int[genotype.genotypeLength()];
		msg = genotype.getGenes().getCoordinates();
		log(msg);
	}
	
	public static void log(Genotype[] genotypes) {
		for (int i = 0 ; i < genotypes.length ; i++) {
			log(genotypes[i]);
		}
	}
	
	public static void log(double[] msg) {
		String ch = "(";
		for (int i = 0 ; i < msg.length - 1 ; i++) {
			ch += msg[i] + ", ";
		}
		ch += msg[msg.length - 1] + ")";
		System.out.println(ch);
	}
	
	public static void log(String[] msg) {
		for (int i = 0 ; i < msg.length ; i++) {
			System.out.println("Array[" + i + "] = " + msg[i]);
		}
	}
	
	//Getter
	
	public static int getNLogs() {
		return nLogs;
	}
	
}
