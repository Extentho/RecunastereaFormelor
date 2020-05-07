package ro.usv.rf;

import java.util.Comparator;

public class Iris implements Comparable<Iris>{
	public String[] pattern;
	public Double distance;
	public Iris(String[] pattern, double distance)  {
		this.pattern = pattern;
		this.distance = distance;
	}

	@Override
	public int compareTo(Iris o) {
		// TODO Auto-generated method stub
		return Double.compare(this.distance, o.distance);
	}
}
