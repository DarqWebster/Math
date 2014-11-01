package darq.math.geometry;

import darq.math.Const;
import darq.math.Utils;
import darq.math.exception.MathException;

/**
 *
 * @author Craig.Webster
 */
public class Line {
	public final double yCo;
	public final double xCo;
	public final double con;

	public Line(double yCo, double xCo, double con) throws MathException {
		// (yCo * y) + (xCo * x) + con = 0;
		if (Utils.equals(yCo, 0) && Utils.equals(xCo, 0)) {
			throw new MathException("A line in standard form cannot have both the y and x coefficients be zero, this suggests that the line is simply a constant with no y or x value nor gradient, which is impossible.");
		}

		this.yCo = yCo;
		this.xCo = xCo;
		this.con = con;
	}

	public Line(double gradient, Point point) {
		// y - y1 = m * (x - x1)
		if (Utils.equals(gradient, Const.VERTICAL_GRADIENT)) {
			yCo = 0;
			xCo = -1;
			con = point.x;
		} else {
			yCo = 1;
			xCo = -gradient;
			con = (gradient * point.x) - point.y;
		}
	}

	public Line(Point point1, Point point2) {
		// (x2 - x1)(y - y1) = (y2 - y1)(x - x1)
		double deltaY = point2.y - point1.y;
		double deltaX = point2.x - point1.x;
		yCo = deltaX;
		xCo = -deltaY;
		con = (deltaX * -point1.y) - (deltaY * -point1.x);
	}

	public double getY(double x) {
		if (Utils.equals(yCo, 0)) {
			return Double.NaN;
		}
		return -(((x * xCo) + con) / yCo);
	}

	public double getX(double y) {
		if (Utils.equals(xCo, 0)) {
			return Double.NaN;
		}
		return -(((y * yCo) + con) / xCo);
	}

	public double getGradient() {
		return yCo == 0 ? Const.VERTICAL_GRADIENT : -xCo / yCo;
	}

	public boolean contains(Point point) {
		return ((point.y * xCo) + (point.x * yCo) + con) == 0;
		//return (Utils.equals(yCo, 0) || Utils.equals(getY(point.x), point.y)) && (Utils.equals(xCo, 0) || Utils.equals(getX(point.y), point.x));
	}

	@Override
	public String toString() {
		return "Line{" + "yCo=" + yCo + ", xCo=" + xCo + ", con=" + con + '}';
	}
}
