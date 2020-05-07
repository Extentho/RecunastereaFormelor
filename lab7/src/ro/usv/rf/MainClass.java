package ro.usv.rf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class MainClass {
	
	
	public static void main(String[] args) {
		String[][] stringLearningSet;
		double[][] learningSet;
		double[][] distanceMatrix;
		double[][] avgMatrix;
//		String[] classes = new String[3];
		ArrayList<String> classes = new ArrayList<>();
		
//		double[][] toFind = {
//				{25.89, 47.56},
//				{24, 45.15},
//				{25.33, 45.44}
//		};
		
		double[][] toFind = {
				{1, 3},
				{4, 5},
				{0, 0}
		};
		
		try {
//			stringLearningSet = FileUtils.readLearningSetFromFile("data.csv");
			stringLearningSet = FileUtils.readLearningSetFromFile("in.txt");
			learningSet = getDoubleLearningSet(stringLearningSet);
//			classes = getClassesArray(stringLearningSet);
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			
			avgMatrix = getAvgMatrix(stringLearningSet, classes);
			for(int i=0; i<avgMatrix.length;i++) {
				for(int j=0; j<avgMatrix[i].length;j++) {
					System.out.print(avgMatrix[i][j]+ " ");
				}
				System.out.println();
			}
	
			for(int i=0; i<toFind.length;i++) {
				double[] psi = new double[avgMatrix.length];
				
				for(int j=0;j<avgMatrix.length;j++) {
					psi[j] = avgMatrix[j][0]*toFind[i][0] + avgMatrix[j][1]*toFind[i][1] + avgMatrix[j][2];
				}
				
				double max = psi[0];
				int indexMax = 0;
				for(int j=1;j<psi.length;j++) {
					if(psi[j]>max) {
						max = psi[j];
						indexMax = j;
					}
				}
				System.out.println("Forma " + i + " apartine de clasa " + classes.get(indexMax) + " (psi=" + psi[indexMax] + ")");
			}

		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
		
	}
	
	private static void writeClass(int neighboursNumber, List<Place> nearest) {
		System.out.print(neighboursNumber + " neighbours: ");
		HashMap<String, Integer> nearestMap = new HashMap<>();
		for(int j=0; j<neighboursNumber;j++) {
			Place peek = nearest.get(j);
			String name = peek.pattern[peek.pattern.length -1];
			if(!nearestMap.containsKey(name)) {
				nearestMap.put(name, 0);
			}
			nearestMap.put(name, nearestMap.get(name) +1);
		}
		
		System.out.println(Collections.max(nearestMap.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
	}
	
	private static double[][] getDoubleLearningSet(String[][] learningSet){
		double[][] doubleLearningSet = new double[learningSet.length][];
		for(int i=0; i< learningSet.length;i++) {
			doubleLearningSet[i] = new double[learningSet[i].length];
			for(int j=0; j<learningSet[i].length; j++) {
				try {
					doubleLearningSet[i][j] = Double.valueOf(learningSet[i][j]);
				} catch(NumberFormatException| ArrayIndexOutOfBoundsException e ) {
					
				}
			}
		}
		
		return doubleLearningSet;
	}
	
	private static String[] getClassesArray(String[][] learningSet) {
		String[] array = new String[learningSet.length];
		for(int i=0; i<learningSet.length;i++) {
			array[i] =learningSet[i][learningSet[i].length -1]; 
			//System.out.println(array[i]);
		}
		return array;
	}
	
	private static double[][] getAvgMatrix(String[][] learningSet, ArrayList<String> classes){
		double[][] avgMatrix;
		TreeMap<String, ArrayList<Double>> hMapX = new TreeMap<>();
		TreeMap<String, ArrayList<Double>> hMapY = new TreeMap<>();
		
//		for( int i=0; i<learningSet.length;i++) {
		for( int i=1; i<learningSet.length;i++) {
			String key = learningSet[i][learningSet[i].length -1];
			if(!hMapX.containsKey(key)) {
				hMapX.put(key, new ArrayList<Double>());
				hMapY.put(key, new ArrayList<Double>());
			}
			hMapX.get(key).add(Double.valueOf(learningSet[i][0]));
			hMapY.get(key).add(Double.valueOf(learningSet[i][1]));
		}
		
		avgMatrix = new double[hMapX.size()][];
		for(int i =0; i< avgMatrix.length;i++) {
			avgMatrix[i] = new double[3];
			 
			avgMatrix[i][0] = ((ArrayList<Double>) hMapX.values().toArray()[i]).stream().mapToDouble(a-> a).average().getAsDouble();
			avgMatrix[i][1] = ((ArrayList<Double>) hMapY.values().toArray()[i]).stream().mapToDouble(a-> a).average().getAsDouble();
			avgMatrix[i][2] = (-1.0/2)*(Math.pow(avgMatrix[i][0], 2) + Math.pow(avgMatrix[i][1], 2));
			classes.add((String)hMapX.keySet().toArray()[i]);
		}
		return avgMatrix;
	}
}
