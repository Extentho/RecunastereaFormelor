package ro.usv.rf;

import java.util.Comparator;

public class Place implements Comparable<Place>{
	public String[] pattern;
	public Double distance;
	public Place(String[] pattern, double distance)  {
		this.pattern = pattern;
		this.distance = distance;
	}

	@Override
	public int compareTo(Place o) {
		// TODO Auto-generated method stub
		return Double.compare(this.distance, o.distance);
	}
}
