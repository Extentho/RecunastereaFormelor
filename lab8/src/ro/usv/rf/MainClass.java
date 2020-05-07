package ro.usv.rf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

public class MainClass {

	public static void main(String[] args) {
		String[][] learningSet;
		try {
			learningSet = FileUtils.readLearningSetFromFile("iris.csv");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length-1;

			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns,
					numberOfFeatures));
			
			Map<String, Integer> classesMap = new HashMap<String, Integer>();
			
			//create map with distinct classes and number of occurence for each class
			for (int i=0; i<numberOfPatterns; i++)
			{
				String clazz = learningSet[i][learningSet[i].length-1];
				if (classesMap.containsKey(clazz))
				{
					Integer nrOfClassPatterns = classesMap.get(clazz);
					classesMap.put(clazz, nrOfClassPatterns + 1);
				}
				else
				{
					classesMap.put(clazz, 1);
				}
			}
			Random random = new Random();
			//map that keeps for each class the random patterns selected for evaluation set
			Map<String, List<Integer>> classesEvaluationPatterns = new HashMap<String, List<Integer>>();
			Integer evaluationSetSize = 0;
			for (Map.Entry<String, Integer> entry: classesMap.entrySet())
			{
				String className = entry.getKey();
				Integer classMembers = entry.getValue();
				Integer evaluationPatternsNr = Math.round(classMembers *15/100);
				evaluationSetSize += evaluationPatternsNr;
				List<Integer> selectedPatternsForEvaluation = new ArrayList<Integer>();
				for (int i=0; i<evaluationPatternsNr; i++)
				{
					Integer patternNr = random.nextInt(classMembers ) +1;
					while (selectedPatternsForEvaluation.contains(patternNr))
					{
						patternNr = random.nextInt(classMembers ) +1;
					}
					selectedPatternsForEvaluation.add(patternNr);
				}
				classesEvaluationPatterns.put(className, selectedPatternsForEvaluation);				
			}
			
			String[][] evaluationSet = new String[evaluationSetSize][numberOfPatterns];
			String[][] trainingSet = new String[numberOfPatterns-evaluationSetSize][numberOfPatterns];
			int evaluationSetIndex = 0;
			int trainingSetIndex = 0;
			Map<String, Integer> classCurrentIndex = new HashMap<String, Integer>();
			for (int i=0; i<numberOfPatterns; i++)
			{
				String className = learningSet[i][numberOfFeatures];
				if (classCurrentIndex.containsKey(className))
				{
					int currentIndex = classCurrentIndex.get(className);
					classCurrentIndex.put(className, currentIndex+1);
				}
				else
				{
					classCurrentIndex.put(className, 1);
				}
				if (classesEvaluationPatterns.get(className).contains(classCurrentIndex.get(className)))
				{
					evaluationSet[evaluationSetIndex] = learningSet[i];
					evaluationSetIndex++;
				}
				else
				{
					trainingSet[trainingSetIndex] = learningSet[i];
					trainingSetIndex++;
				}
			}
			
			FileUtils.writeLearningSetToFile("eval.txt", evaluationSet);
			FileUtils.writeLearningSetToFile("train.txt", trainingSet);
			
			
			// KNN
			int corrects[] = new int[16];			
			for(int i=0; i<evaluationSet.length;i++) {
//				System.out.println(trainingSet[i][0] + " " + trainingSet[i][1]);
				String initialName = evaluationSet[i][evaluationSet[i].length-1];
				System.out.println(initialName);
				PriorityQueue<Iris> pq = new PriorityQueue<Iris>(15, Collections.reverseOrder());
				for(int j=0; j<trainingSet.length; j++) {
					double distance = DistanceUtils.euclidianDistanceGeneralized(evaluationSet[i], trainingSet[j]);
					if(pq.size() <15) {
						pq.add(new Iris(trainingSet[j], distance));
					} else {
						if(distance < ((Iris)pq.peek()).distance) {
							pq.poll();
							pq.add(new Iris(trainingSet[j], distance));
						}
					}
				}
				
				List<Iris> nearest = new ArrayList<>(15);
				while(pq.size()!=0) {
					Iris p = (Iris)pq.poll();
					nearest.add(p);
				}
				nearest.sort(Comparator.naturalOrder());
				for(int j=1;j<=15;j+=2) {
					String nameAfterKNN = getClass(j, nearest);
					if(nameAfterKNN.equals(initialName)) {
						corrects[j]++;
					}
					
				}
			}
			int maxCor = corrects[1];
			int poz = 1;
			for(int i=1; i<corrects.length;i+=2) {
				if(corrects[i]>maxCor) {
					maxCor = corrects[i];
					poz= i;
				}
			}
			
			System.out.println("Optim pt k=" + poz);
			
			
			// Type 3 Classifier
			ArrayList<String> classes = new ArrayList<>();
			double[][] avgMatrix = getAvgMatrix(trainingSet, classes);
					
			
			for(int i=0; i<evaluationSet.length;i++) {
				double[] psi = new double[avgMatrix.length];
				
				for(int j=0;j<avgMatrix.length;j++) {
//					psi[j] = avgMatrix[j][0]*evaluationSet[i][0] + avgMatrix[j][1]*evaluationSet[i][1] + avgMatrix[j][2];
					for(int k=0;k<evaluationSet[i].length-1;k++) {
						psi[j] += avgMatrix[j][k] * Double.valueOf(evaluationSet[i][k]);
					}
					psi[j]+= avgMatrix[j][evaluationSet[i].length-1];
				}
				
				double max = psi[0];
				int indexMax = 0;
//				System.out.println(max);
				for(int j=1;j<psi.length;j++) {
//					System.out.println(psi[j]);
					if(psi[j]>max) {
						max = psi[j];
						indexMax = j;
					}
				}
				System.out.println("Forma " + i + " apartine de clasa " + classes.get(indexMax) + " (psi=" + psi[indexMax] + ")");				
			}
			
			
			for(int i=0; i< 100;i++) {
				System.out.print("[");
				for(int j=0;j<100;j++) {
					System.out.print("0, ");
				}
				System.out.println("]");
			}
		} catch (USVInputFileCustomException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finished learning set operations");
		}
	}
	
	private static String getClass(int neighboursNumber, List<Iris> nearest) {
		System.out.print(neighboursNumber + " neighbours: ");
		HashMap<String, Integer> nearestMap = new HashMap<>();
		for(int j=0; j<neighboursNumber;j++) {
			Iris peek = nearest.get(j);
			String name = peek.pattern[peek.pattern.length -1];
			if(!nearestMap.containsKey(name)) {
				nearestMap.put(name, 0);
			}
			nearestMap.put(name, nearestMap.get(name) +1);
		}
		
		String nameAfterKNN = Collections.max(nearestMap.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
		System.out.println(nameAfterKNN);
		return nameAfterKNN;
	}
	
	private static double[][] getAvgMatrix(String[][] learningSet, ArrayList<String> classes){
		double[][] avgMatrix;
		ArrayList<TreeMap<String, ArrayList<Double>>> hMaps = new ArrayList<TreeMap<String, ArrayList<Double>>>();
		for(int i=0; i<learningSet[0].length-1;i++) {
			hMaps.add(new TreeMap<String, ArrayList<Double>>());
		}

		for( int i=0; i<learningSet.length;i++) {
			String key = learningSet[i][learningSet[i].length -1];
			if(!hMaps.get(0).containsKey(key)) {
				for(int j =0; j<learningSet[i].length -1;j++) {
					hMaps.get(j).put(key, new ArrayList<Double>());
				}
			}
			for(int j =0; j<learningSet[i].length -1;j++) {
				hMaps.get(j).get(key).add(Double.valueOf(learningSet[i][j]));
			}
		}
		
		avgMatrix = new double[hMaps.get(0).size()][];
		for(int i =0; i< avgMatrix.length;i++) {
			avgMatrix[i] = new double[learningSet[0].length];
			
			for(int j=0; j<learningSet[0].length-1;j++) {
				avgMatrix[i][j] = ((ArrayList<Double>) hMaps.get(j).values().toArray()[i]).stream().mapToDouble(a-> a).average().getAsDouble();
				avgMatrix[i][learningSet[0].length-1] -= 0.5*Math.pow(avgMatrix[i][j], 2);
			}

			classes.add((String)hMaps.get(0).keySet().toArray()[i]);
		}
		return avgMatrix;
	}

}
