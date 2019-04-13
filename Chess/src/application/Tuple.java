package application;

public class Tuple<X, Y> {
	public final Integer x;
	public final Integer y;

	public Tuple(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if(x.equals(((Tuple<Integer, Integer>)obj).x)&& y.equals(((Tuple<Integer, Integer>)obj).y)) {
			return true;
		}
		return false;
	}
}
