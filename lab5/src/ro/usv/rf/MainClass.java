package ro.usv.rf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class MainClass {
	
	
	public static void main(String[] args) {
		String[][] stringLearningSet;
		double[][] learningSet;
		double[][] distanceMatrix;
		String[] classes;
		
		double[][] toFind = {
				{25.89, 47.56},
				{24, 45.15},
				{25.33, 45.44}
		};
		
		try {
			// read data from csv
			stringLearningSet = FileUtils.readLearningSetFromFile("data.csv");
			learningSet = FileUtils.getDoubleLearningSet(stringLearningSet);
			classes = FileUtils.getClassesArray(stringLearningSet);
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			
			// iterate over locations
			for(int i=0; i<toFind.length;i++)
			{
				System.out.println(toFind[i][0] + " " + toFind[i][1]);
				// create a priority queue (auto sorted in reverse order)
				PriorityQueue<Location> pq = new PriorityQueue<Location>(31, Collections.reverseOrder());
				for(int j=0; j<learningSet.length; j++) 
				{
					//calculate distance from desired location to every other location
					double distance = DistanceUtils.euclidianDistanceGeneralized(toFind[i], learningSet[j]);
					if(pq.size() <31) 
					{
						pq.add(new Location(stringLearningSet[j], distance));
					} 
					else 
					{
						if(distance < ((Location)pq.peek()).distance)
						{
							pq.poll();
							pq.add(new Location(stringLearningSet[j],distance));
						}
					
					}
				}
				
				List<Location> nearest = new ArrayList<>(31);
				while(pq.size()!=0) {
					Location p = (Location)pq.poll();
					nearest.add(p);
				}
				// mic la mare functie de distanta
				nearest.sort(Comparator.naturalOrder());
	//			guessCountyOfLocation(9,nearest);
//			guessCountyOfLocation(11,nearest);
//				wriguessCountyOfLocationteClass(17,nearest);
				guessCountyOfLocation(31,nearest);

		}
					
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
		
	}
	
	private static void guessCountyOfLocation(int neighboursNumber, List<Location> nearest) 
	{
		System.out.print("Location: has "+ neighboursNumber + " neighbours in county: ");
		HashMap<String, Integer> nearestMap = new HashMap<>();
		for(int j=0; j<neighboursNumber;j++) {
			Location peek = nearest.get(j);
			String name = peek.pattern[peek.pattern.length -1];
			if(!nearestMap.containsKey(name)) 
			{
				nearestMap.put(name, 0);
			}
			nearestMap.put(name, nearestMap.get(name) +1);
		}
		System.out.println(Collections.max(nearestMap.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey());
	}	
}
