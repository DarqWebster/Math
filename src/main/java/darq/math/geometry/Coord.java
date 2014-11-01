package darq.math.geometry;

/**
 *
 * @author Craig.Webster
 */
public class Coord {
	public final int y;
	public final int x;
	
	public Coord(int y, int x) {
		this.y = y;
		this.x = x;
	}
	
	public boolean equals(Coord coord) {
		return this.y == coord.y && this.x == coord.x;
	}
	
	public Point toPoint() {
		return new Point(y, x);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + this.y;
		hash = 59 * hash + this.x;
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
		final Coord other = (Coord) obj;
		if (this.y != other.y) {
			return false;
		}
		if (this.x != other.x) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Coord{y=" + y + ", x=" + x + '}';
	}
}
