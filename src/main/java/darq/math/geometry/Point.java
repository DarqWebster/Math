package darq.math.geometry;

import darq.math.Utils;

/**
 *
 * @author Craig.Webster
 */
public class Point {
	public final double y;
	public final double x;

	public Point(double y, double x) {
		this.y = y;
		this.x = x;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		hash = 37 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point other = (Point) obj;
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
			return false;
		}
		return true;
	}

	

	@Override
	public String toString() {
		return "Point{" + "y=" + y + ", x=" + x + '}';
	}
}
