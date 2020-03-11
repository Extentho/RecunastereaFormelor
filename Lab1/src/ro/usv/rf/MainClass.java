package ro.usv.rf;

public class MainClass {

	public static void main(String[] args) {
		double[][] learningSet = FileUtils.readLearningSetFromFile("in.txt");
		FileUtils.writeLearningSetToFile("out.csv", normalizeLearningSet(learningSet));
		System.out.println("Done exporting normalized matrix !");
	}

	private static double[][] normalizeLearningSet(double[][] learningSet) {
		// set sizes of the arrays and matrix we will use
		
		double[][] normalizedLearningSet = new double[learningSet.length][learningSet[0].length];
		//array the size of column number holding min values
		double[] min = new double[learningSet[0].length];
		//array the size of column number holding max values
		double[] max = new double[learningSet[0].length];
		
        //first iterate over each column of the input matrix
		for (int j = 0; j < learningSet[0].length; j++) 
		{
			//initialize min / max with first element of the column
			min[j] = max[j] = learningSet[0][j];
			//then iterate over every row in that column
			for (int i = 0; i < learningSet.length; i++) 
			{
				//if we found a smaller element, replace min in array of mins
				//if we found a bigger element, replace max in array of maxes
				min[j] = Double.min(min[j], learningSet[i][j]);
				max[j] = Double.max(max[j], learningSet[i][j]);
			}
		}
		//finally compute the normalization for each element
		//and set the matrix to be written in the output file 
		for (int i = 0; i < learningSet.length; i++) 
		{
			for (int j = 0; j < learningSet[0].length; j++) 
			{
				normalizedLearningSet[i][j] = (learningSet[i][j] - min[j]) / (max[j] - min[j]);
			}
		}
		return normalizedLearningSet;
	}
}
