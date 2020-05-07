package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainClass {
	
	
	public static void main(String[] args) {
		double[][] learningSet;
		double[][] matrixDistance;
	
		try {
			// read entry set
			learningSet = FileUtils.readLearningSetFromFile("iin.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length-1;
			int patternIndex = learningSet.length - 1;		
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			
			
			// calculate euclidian distance matrix between patterns
			double [][]distanceMatrix = new double [numberOfPatterns][numberOfPatterns];
			for (int i=0; i<numberOfPatterns; i++)
			{
				double[] firstPattern = learningSet[i];
				for (int j=0; j<numberOfPatterns-1; j++)
				{
					double[] otherPattern = learningSet[j];
					distanceMatrix[i][j] = distanceMatrix [j][i] = Distances.calculateEuclidianDistance(firstPattern, otherPattern);
				}
			}
			FileUtils.writeLearningSetToFile("out.txt", distanceMatrix);
			
									
			int emptyClassPattern = 0;
			for (int i = 0 ; i < numberOfPatterns ; i++)
			{
					if (distanceMatrix[i][numberOfFeatures] == 0)
						emptyClassPattern= i;
			}
			 
			double minDist = distanceMatrix[emptyClassPattern][0];
			for ( int i = 0 ; i < distanceMatrix[0].length; i++)
			{
				
				if (distanceMatrix[emptyClassPattern][i] < minDist && distanceMatrix[emptyClassPattern][i] != 0)
				{
					minDist = distanceMatrix[emptyClassPattern][i];
				}
			}
			System.out.println("For Pattern "+emptyClassPattern +" the class is: "+ learningSet[1][learningSet[0].length -1]);
			
		} 
		// exception handling
		catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
			}
		finally {
			System.out.println("Finished learning set operations");
		}
	}
}
