package ro.usv.rf;

import java.util.Comparator;

public class Location implements Comparable<Location>{
	public String[] pattern;
	public Double distance;
	public Location(String[] pattern, double distance)  {
		this.pattern = pattern;
		this.distance = distance;
	}

	@Override
	public int compareTo(Location o) {
		// TODO Auto-generated method stub
		return Double.compare(this.distance, o.distance);
	}
}
