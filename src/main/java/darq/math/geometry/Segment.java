package darq.math.geometry;

/**
 *
 * @author Craig.Webster
 */
public class Segment {
	public final Point pS;
	public final Point pE;

	public Segment(Point pS, Point pE) {
		this.pS = pS;
		this.pE = pE;
	}

	@Override
	public String toString() {
		return "LineSegment{" + "pS=" + pS + ", pE=" + pE + '}';
	}
}
