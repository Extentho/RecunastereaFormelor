package ro.usv.rf;

import java.util.HashMap;
import java.util.Map;

public class StatisticsUtils {

	protected static double calculateFeatureAverage(Double[] feature) {
		Map<Double, Integer> counterMap = getFeatureDistincElementsCounterMap(feature);
		double featureAverage = 0;

		double sum1 = 0;
		double sum2 = 0;

		sum1 = counterMap.keySet().stream().mapToDouble(x -> calculateSum1(x, counterMap.get(x))).sum();
		sum2 = counterMap.values().stream().mapToInt(x -> x).sum();
		featureAverage = sum1 / sum2;
		System.out.println("The feature average is: " + featureAverage);
		return featureAverage;
	}

	protected static Map<Double, Integer> getFeatureDistincElementsCounterMap(Double feature[]) {
		Map<Double, Integer> counterMap = new HashMap<Double, Integer>();
		for (int j = 0; j < feature.length; j++) {
			if (counterMap.containsKey(feature[j])) {
				int count = counterMap.get(feature[j]);
				counterMap.put((feature[j]), ++count);
			} else {
				counterMap.put((feature[j]), 1);
			}
		}
		return counterMap;
	}

	protected static Map<Double, Double> getWeightsMap(Double feature[], Double weights[]) {
		Map<Double, Double> weightsMap = new HashMap<Double, Double>();
		for (int j = 0; j < feature.length; j++) {
			if (weightsMap.containsKey(feature[j])) {
				double weight = weightsMap.get(feature[j]);
				weightsMap.put((feature[j]), weight + weights[j]);
			} else {
				weightsMap.put((feature[j]), weights[j]);
			}
		}
		return weightsMap;
	}
	
	private static Double calculateSum1(double value, int count) {
		return count * value;
	}

	private static Double calculateSum1(double value, double weight) {
		return weight * value;
	}

	protected static double calculateFeatureWeightedAverage(Double[] feature, Double[] weights) {
		double featureWeightedAverage = 0.0;
		double sum1 = 0;
		double sum2 = 0;
		for (int i = 0; i < feature.length; i++) {
			sum1 += feature[i] * weights[i];
			sum2 += weights[i];
		}
		featureWeightedAverage = sum1/sum2;
		return featureWeightedAverage;
	}

	protected static double calculateFeatureWeightedAverageWithStreams(Double[] feature, Double[] weights) {
		double featureWeightedAverage = 0.0;
		Map<Double, Double> weightsMap = getWeightsMap(feature, weights);
		double sum1 =  weightsMap.keySet().stream().mapToDouble(x -> calculateSum1(x, weightsMap.get(x))).sum();
		double sum2 = weightsMap.values().stream().mapToDouble(x -> x).sum();
		featureWeightedAverage = sum1 / sum2;
		return featureWeightedAverage;
	}

	protected static double calculateFrequencyOfOccurence(Map<Double, Integer> counterMap, double featureElement) {
		double frequencyOfOccurence = Double.valueOf(counterMap.get(featureElement))
				/ counterMap.values().stream().mapToInt(x -> x).sum();
		return frequencyOfOccurence;
	}

//	protected static double calculateFeatureDispersion(Double[] feature, double featureWeightedAverage) {
//		double featureDispersion = 0.0;
//		double sum = 0;
//		for (int k = 0; k<feature.length; k++)
//		{
//			sum += Math.pow(feature[k]-featureWeightedAverage, 2);
//		}
//		featureDispersion = (Double.valueOf(1)/(feature.length-1))*sum;
//		return featureDispersion;
//	}
//	
	protected static double calculateFeatureDispersion(Double[] feature1,Double[] feature2, double featureWeightedAverage1, double featureWeightedAverage2) {
		double featureDispersion = 0.0;
		double sum = 0;
		for (int k = 0; k<feature1.length; k++)
		{
			sum += Math.pow(feature1[k]-featureWeightedAverage1, 2);
		}
		featureDispersion = (Double.valueOf(1)/(feature1.length-1))*sum;
		
		for (int k = 0; k<feature2.length; k++)
		{
			sum += Math.pow(feature2[k]-featureWeightedAverage2, 2);
		}
		featureDispersion += (Double.valueOf(1)/(feature2.length-1))*sum;
		
		return featureDispersion ;
	}
	

	protected static double calculateCovariance(Double[] feature1, Double[] feature2, double feature1WeightedAverage,
			double feature2WeightedAverage) {
		double covariance = 0.0;
		double sum = 0;
		for (int k = 0; k<feature1.length; k++)
		{
			sum += (feature1[k]-feature1WeightedAverage)*(feature2[k]-feature2WeightedAverage);
		}
		covariance = (Double.valueOf(1)/(feature1.length-1))*sum;
		return covariance;
	}

	protected static double calculateCorrelationCoefficient(double covariance, double feature1Dispersion,
			double feature2Dispersion) {
		double correlationCoefficient = covariance/Math.sqrt(feature1Dispersion*feature2Dispersion);
		return correlationCoefficient; 
	}

	protected static double calculateAverageSquareDeviation(double featureDispersion) {
		double averageSquareDeviation = Math.sqrt(featureDispersion);
		return averageSquareDeviation;
	}
}
