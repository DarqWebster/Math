package darq.math.geometry;

import darq.math.exception.MathException;

/**
 *
 * @author Craig.Webster
 */
public class Ray {
	public final Point point;
	public final UnitVector vector;

	public Ray(Point point, UnitVector vector) throws MathException {
		this.point = point;
		this.vector = vector;
	}

//	public double getY(double x) {
//		if ((x - point.x) * vector.xVector < 0) {
//			return Double.NaN;
//		}
//		return vector.line.getY(x);
//	}
//
//	public double getX(double y) {
//		if ((y - point.y) * vector.yVector < 0) {
//			return Double.NaN;
//		}
//		return vector.line.getX(y);
//	}
	
	public Point getPointAtDistance(double distance) {
		return new Point(point.y + (distance * vector.yVector), point.x + (distance * vector.xVector));
	}

	@Override
	public String toString() {
		return "Ray{" + "point=" + point + ", vector=" + vector + '}';
	}
}
