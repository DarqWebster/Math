package darq.math.geometry;

/**
 *
 * @author Craig.Webster
 */
public class AxisAlignedBoundingBox {
	public final double yS;
	public final double xS;
	public final double yE;
	public final double xE;

	public AxisAlignedBoundingBox(double yS, double xS, double yE, double xE) {
		this.yS = yS;
		this.xS = xS;
		this.yE = yE;
		this.xE = xE;
	}
}
