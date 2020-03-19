package ro.usv.rf;

public class Distances {

	static double calculateEuclidianDistance(double pattern1 [], double pattern2 [])
	{
		double sum = 0.0;
		for(int i=0; i<pattern1.length; i++) 
		{
			sum += Math.pow((pattern1[i]-pattern2[i]), 2);
		}
		return Math.floor(Math.sqrt(sum)*100)/100;
	}
}
