package ro.usv.rf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class MainClass {
	
	
	public static void main(String[] args) {
		int MAX_KNN = 17;
		String[][] stringLearningSet;
		double[] toFind = { 3.80, 5.75, 6.25,7.25, 8.5};
		try {
			
			//read data set
			stringLearningSet = FileUtils.readLearningSetFromFile("gradesClasses.txt");
			double[] learningSet = new double[stringLearningSet.length];
			String[] classes = new String[stringLearningSet.length];
			
			for(int i=0; i<stringLearningSet.length;i++) {
				learningSet[i] = Double.valueOf(stringLearningSet[i][0]);
				classes[i] = stringLearningSet[i][1];
			}
			
			
			int numberOfPatterns = stringLearningSet.length;
			int numberOfFeatures = stringLearningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			
			
			//calculate distance 
			for(int i=0; i<toFind.length;i++) {
				PriorityQueue<Place> pq = new PriorityQueue<Place>(MAX_KNN, Collections.reverseOrder());
				for(int j=0; j<learningSet.length; j++) {
					double distance = DistanceUtils.euclidianDistanceOneDimension(toFind[i], learningSet[j]);
					if(pq.size() < MAX_KNN)
					{
						pq.add(new Place(stringLearningSet[j], distance));
					} 
					else 
					{
						if(distance < ((Place)pq.peek()).distance) {
							pq.poll();
							pq.add(new Place(stringLearningSet[j], distance));
						}
					}
				}
				// create a list of 17 nearest neighbors
				List<Place> nearest = new ArrayList<>(MAX_KNN);
				while(pq.size()!=0) {
					Place p = (Place)pq.poll();
					nearest.add(p);
				}
				nearest.sort(Comparator.naturalOrder());
				
				 // find maximum value of k for an accurate result
				String firstNeighbor="";
				String current ="";
				int occurence1=0;
				int occurence2=-1;
				
				System.out.println("Pattern: " + toFind[i]);
				for(int k = 1; k<=MAX_KNN; k+=2) 
				{
					current = getClassOfNeighbors(k, nearest);
					if(current.equals(firstNeighbor))
						occurence1++;
					else occurence2++;
					if(firstNeighbor.isEmpty()) 
					{
						firstNeighbor = current;
						occurence1++;
					}
					
					System.out.println(current);				
				}
				if(occurence2 > occurence1)
				{
					System.out.println("Maximum k for which this classification is accurate: " + occurence1);
				}
				
			}
								
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}
	
	private static String getClassOfNeighbors(int neighboursNumber, List<Place> nearest) {
		System.out.print(neighboursNumber + " neighbours: ");
		HashMap<String, Integer> nearestMap = new HashMap<>();
		
		for(int j=0; j<neighboursNumber;j++) 
		{
			Place peek = nearest.get(j);
			String name = peek.pattern[peek.pattern.length -1];
			if(!nearestMap.containsKey(name))
			{
				nearestMap.put(name, 0);
			}
			nearestMap.put(name, nearestMap.get(name) +1);
		}
		return Collections.max(nearestMap.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
	}

}
