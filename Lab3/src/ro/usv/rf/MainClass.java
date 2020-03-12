package ro.usv.rf;

public class MainClass {
	
	
	public static void main(String[] args) {
		double[][] learningSet;
		try {
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patters and %s features", numberOfPatterns, numberOfFeatures));
			
			double[] firstPattern = learningSet[0];
			for (int i=1; i<numberOfPatterns; i++)
			{
				double[] otherPattern = learningSet[i];
				double euclidianDistance = DistanceUtils.calculateEuclidianDistance(firstPattern, otherPattern);
				System.out.println("The Euclidian distance between first pattern and "+ Integer.valueOf(i+1)+ " is: "+ euclidianDistance);
				double cebasevDistance = DistanceUtils.calculateCebasevDistance(firstPattern, otherPattern);
				System.out.println("The  Cebasev  distance between first pattern and "+ Integer.valueOf(i+1)+ " is: "+ cebasevDistance);
				double mahalanobisDistance = DistanceUtils.calculateMahalanobisDistance(firstPattern, otherPattern,2);
				System.out.println("The Mahalanobis distance between first pattern and "+ Integer.valueOf(i+1)+ " is: "+ mahalanobisDistance);
				double cityBlockDistance = DistanceUtils.calculateCityBlockDistance(firstPattern, otherPattern);
				System.out.println("The City Block distance between first pattern and "+ Integer.valueOf(i+1)+ " is: "+ cityBlockDistance);
			}
		} catch (USVInputFileCustomException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Finished learning set operations");
		}
	}

}
