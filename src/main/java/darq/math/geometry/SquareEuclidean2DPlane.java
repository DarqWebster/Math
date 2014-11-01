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
}
