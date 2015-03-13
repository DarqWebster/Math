package darq.math.geometry;

/**
 *
 * @author Craig
 */
public class Triangle extends Polygon {
	public final Point point1;
	public final Point point2;
	public final Point point3;
	
	public Triangle(Point point1, Point point2, Point point3) {
		super(point1, point2, point3);
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
	}
}
