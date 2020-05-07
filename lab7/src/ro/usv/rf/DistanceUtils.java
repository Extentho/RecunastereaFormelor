package ro.usv.rf;

public class DistanceUtils {
	protected static double euclidianDistance(double[] firstCharacteristic, double[] secondCharacteristic) {
		return Math.sqrt( Math.pow(firstCharacteristic[0] - secondCharacteristic[0], 2) + Math.pow(firstCharacteristic[1] - secondCharacteristic[1], 2));
	}
	
	protected static double euclidianDistanceGeneralized(double[] firstCharacteristic, double[] secondCharacteristic) {
		double sum =0;
		for(int i=0; i<firstCharacteristic.length;i++) {
			sum += Math.pow(firstCharacteristic[i] - secondCharacteristic[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	protected static double mahalanobisDistance(double[] firstCharacteristic, double[] secondCharacteristic, int  characteristicsNumber) {
		double sum =0;
		
		for(int i=0; i<firstCharacteristic.length;i++) {
			sum += Math.pow(firstCharacteristic[i] -secondCharacteristic[i] , characteristicsNumber);
		}
		
		return Math.pow(sum, 1.0/characteristicsNumber);
	}
	
	protected static double cebisevDistance(double[] firstCharacteristic, double[] secondCharacteristic) {
		double max = Math.abs(firstCharacteristic[0] -secondCharacteristic[0]);
		for(int i=1; i<firstCharacteristic.length;i++) {
			max = Double.max(max, Math.abs(firstCharacteristic[i] -secondCharacteristic[i]));
		}
		
		return max;
	}
	
	protected static double cityBlockDistance(double[] firstCharacteristic, double[] secondCharacteristic) {
		double sum = 0;
	
		for(int i=0; i<firstCharacteristic.length;i++) {
			sum +=  Math.abs(firstCharacteristic[i] -secondCharacteristic[i]);
		}
		
		return sum;
	}
}
