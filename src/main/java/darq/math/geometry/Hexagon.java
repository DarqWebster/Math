package darq.math.geometry;

/**
 *
 * Represents a hexagon, and provides calculations based on hexagons.
 * Instantiating the <code>Hexagon</code> simply runs all calculations
 * and stores them in <code>public final</code> constants.
 *    _______
 *   /   |  /\
 *  /    A B  \
 * /     |/    \
 * \     |     /
 *  \    |    /
 *   \___|___/
 * 
 * A: RADIUS_EDGE.
 * B: RADIUS_VERT.
 * 
 * @author Craig.Webster
 */
public class Hexagon {
	/**
	 * The distance from the centre to the closest point on any side.
	 */
	public final double RADIUS_EDGE;
	/**
	 * The distance from the centre to any corner.
	 * The length of any side.
	 */
	public final double RADIUS_VERT;

	/**
	 * Math.PI / 12 radians == 30 degrees.
	 */
	private static final double THIRTY_DEGREES = Math.PI / 6;
	private static final double COS_OF_30_DEGREES = Math.cos(THIRTY_DEGREES);

	private Hexagon(double radiusEdge, double radiusVert) {
		RADIUS_EDGE = radiusEdge;
		RADIUS_VERT = radiusVert;
	}

	public static Hexagon constructWithRadiusEdge(double radiusEdge) {
		return new Hexagon(radiusEdge, getRadiusVertFromRadiusEdge(radiusEdge));
	}

	public static Hexagon constructWithRadiusVert(double radiusVert) {
		return new Hexagon(getRadiusEdgeFromRadiusVert(radiusVert), radiusVert);
	}
	
	public static double getRadiusVertFromRadiusEdge(double radiusEdge) {
		/*
		 * cos(a) = adj / hyp;
		 * cos(30) = RADIUS_EDGE / RADIUS_VERT;
		 * RADIUS_VERT = RADIUS_EDGE / cos(30);
		 */
		return (radiusEdge / COS_OF_30_DEGREES);
	}
	
	public static double getRadiusEdgeFromRadiusVert(double radiusVert) {
		/*
		 * cos(a) = adj / hyp;
		 * cos(30) = RADIUS_EDGE / RADIUS_VERT;
		 * RADIUS_EDGE = cos(30) * RADIUS_VERT;
		 */
		return (COS_OF_30_DEGREES * radiusVert);
	}

	@Override
	public String toString() {
		return "Hexagon{" + "RADIUS_EDGE=" + RADIUS_EDGE + ", RADIUS_VERT=" + RADIUS_VERT + '}';
	}
}
