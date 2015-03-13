package darq.math.geometry;

/**
 *
 * @author Craig.Webster
 */
public class SquareEuclidean2DPlane extends Abstract2DPlane {	
	@Override
	public double distance(double yD, double xD) {
		return Math.sqrt(yD * yD + xD * xD);
	}
	
	// TODO: Not sure if dotproduct is generalisable to Abstract2DPlane, but probably not.
	public double dotproduct(Segment s1, Segment s2) {
		return (s1.pE.y - s1.pS.y) * (s2.pE.y - s2.pS.y) + (s1.pE.x - s1.pS.x) * (s2.pE.x - s2.pS.x);
	}
}
