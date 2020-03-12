package ro.usv.rf;

public class DistanceUtils {
	protected static double calculateEuclidianDistance(double [] pattern1, double [] pattern2) {
		double sum = 0.0;
		for(int i=0; i<2; i++) 
		{
			sum = Math.pow((pattern1[i]-pattern2[i]), 2) + Math.pow((pattern1[i+1]-pattern2[i+1]), 2);
		}
		return Math.sqrt(sum);
		
	}
	
	protected static double calculateCebasevDistance(double [] pattern1, double [] pattern2) {
		double max = 0.0;
		for(int i=0; i < pattern1.length; i++) 
		{
			max = Double.max(max, Math.abs(pattern1[i]- pattern2[i]));
		}
		return max;
		
	}
	protected static double calculateMahalanobisDistance(double[] pattern1,double[] pattern2, int n) 
	{
	       
        double sum=0.0;
        for(int i=0; i<pattern1.length; i++) {
            sum += Math.pow(pattern1[i]-pattern2[i], n);
        }
        return Math.pow(sum, (double)1/n);
    }
	protected static double calculateCityBlockDistance(double [] pattern1, double [] pattern2) {
		double sum = 0.0;
		for(int i=0; i < pattern1.length; i++) 
		{
			sum += Math.abs(pattern1[i] - pattern2[i]);
		}
		return sum;
		
	}
}

