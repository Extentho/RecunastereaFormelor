package ro.usv.rf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainClass {
	
	
	public static void main(String[] args) {
		double[][] learningSet;
		double[][] matrixDistance;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			int patternIndex = learningSet.length - 1;		
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			double [][]distanceMatrix = new double [numberOfPatterns][numberOfPatterns];
			for (int i=0; i<numberOfPatterns; i++)
			{
				double[] firstPattern = learningSet[i];
				for (int j=0; j<numberOfPatterns; j++)
				{
					double[] otherPattern = learningSet[j];
					distanceMatrix[i][j] = distanceMatrix [j][i] = Distances.calculateEuclidianDistance(firstPattern, otherPattern);
				}
			}
			
			int closestPatternIndex = 0;
			double minDistance = distanceMatrix[patternIndex][closestPatternIndex];
			for ( int i = 0; i <numberOfPatterns; i++)
			{
				if ((i != patternIndex) && distanceMatrix[patternIndex][i] < minDistance)
				{
					minDistance = distanceMatrix[patternIndex][i];
					closestPatternIndex = i;
				}
			}
			System.out.println("Pattern class is: "+ learningSet[patternIndex][learningSet[0].length - 1]);
			FileUtils.writeLearningSetToFile("out.txt", distanceMatrix);
		} 
		catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
			}
		finally {
			System.out.println("Finished learning set operations");
		}
	}
		
}
