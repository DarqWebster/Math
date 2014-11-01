package darq.math;

import darq.math.geometry.Line;
import darq.math.geometry.Point;

/**
 *
 * @author Craig.Webster
 */
public class Utils {
	public static long roundOpt(double value) {
		return (long) Math.floor(value + 0.5);
	}
	
	public static long roundPes(double value) {
		return (long) Math.ceil(value - 0.5);
	}
	
	public static long roundOptSymmetric(double value) {
		if (value >= 0) {
			return roundOpt(value);
		} else {
			return roundPes(value);
		}
	}
	
	public static long roundPesSymmetric(double value) {
		if (value >= 0) {
			return roundPes(value);
		} else {
			return roundOpt(value);
		}
	}
	
	public static double min(double... values) {
		double min = Double.POSITIVE_INFINITY;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
	
	public static double max(double... values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double value : values) {
			if (value > max) {
				max = value;
			}
		}
		return max;
	}
	
	public static int getNumberAtPower(double value, int base, int power) {
		if (power >= 0) {
			for (int i = 0; i < power; i++) {
				value /= base;
			}
		} else {
			for (int i = power; i < 0; i++) {
				value *= base;
			}
		}
		return ((int) value % base);
	}
	
	public static int sign(double value) {
		return (int) (value / Math.abs(value));
	}
	
	/**
	 * Wraps the Math.round() method to ensure symmetric results regardless of whether the rounded point is positive or negative.
	 * Without this, 0.5 is rounded to 1 while -0.5 is rounded to 0.
	 * @param point
	 * @return 
	 */
	public static long approximate(double point) {
		if (point >= 0) {
			return Math.round(point);
		} else {
			return -Math.round(Math.abs(point));
		}
	}
	
	public static long pessimisticApproximate(double point) {
		if (point >= 0) {
			return (long) Math.floor(point);
		} else {
			return (long) Math.ceil(point);
		}
	}
	
	public static double closest(double reference, double doubleOne, double doubleTwo) {
		return Math.abs(reference - doubleOne) <= Math.abs(reference - doubleTwo) ? doubleOne : doubleTwo;
	}
	
	private Point difference(Point point, Line line) {
		double yDelta = point.y - line.getY(point.x);
		double xDelta = point.x - line.getX(point.y);
		return new Point(yDelta, xDelta);
	}
	
	public static Point intersect(Line line1, Line line2) {
		double y = Double.NaN;
		double x = Double.NaN;
		
		if (!equals(line1.getGradient(), line2.getGradient())) {
			if (!equals(line1.xCo, 0) && !equals(line2.xCo, 0)) {
				y = ((line1.con / line1.xCo) - (line2.con / line2.xCo)) / ((line2.yCo / line2.xCo) - (line1.yCo / line1.xCo));
			}
			if (!equals(line1.yCo, 0) && !equals(line2.yCo, 0)) {
				x = ((line1.con / line1.yCo) - (line2.con / line2.yCo)) / ((line2.xCo / line2.yCo) - (line1.xCo / line1.yCo));
			}
			if (y == Double.NaN && x != Double.NaN) {
				y = line1.getY(x);
			}
			if (x == Double.NaN && y != Double.NaN) {
				x = line1.getX(y);
			}
		}
		
		return new Point(y, x);
	}
	
	public static boolean equals(double double1, double double2) {
		// TODO: NaN, infinities, negative and positive.
		if (Double.isNaN(double1) && Double.isNaN(double2)) {
			return true;
		}
		if (Double.isInfinite(double1) && Double.isInfinite(double2)) {
			return true;
		}
		return (Math.abs(double1 - double2) < Const.EPSILON);
	}
	
	public static boolean lt(double d1, double d2) {
		return d1 < d2 && !equals(d1, d2);
	}
	
	public static boolean lte(double d1, double d2) {
		return d1 <= d2 || equals(d1, d2);
	}
	
	public static boolean gt(double d1, double d2) {
		return d1 > d2 && !equals(d1, d2);
	}
	
	public static boolean gte(double d1, double d2) {
		return d1 >= d2 || equals(d1, d2);
	}
	
	public static boolean equals(Point point1, Point point2) {
		return (equals(point1.y, point2.y) && equals(point1.x, point2.x));
	}
	
	public static long modulus(long dividend, long divisor) {
		return ((dividend % divisor) + divisor) % divisor;
	}
	
	public static double modulus(double dividend, double divisor) {
		return ((dividend % divisor) + divisor) % divisor;
	}
	
	public static long summation(long from, long to) {
		long sum = 0;
		for (long i = from; i <= to; i++) {
			sum += i;
		}
		return sum;
	}
	
	public static long factorial(long value) {
		if (value < 0) {
			throw new ArithmeticException("Factorial of a negative number.");
		}
		if (value == 0) {
			return 1;
		}
		return value * factorial(value - 1);
	}
	
	public static long lowestCommonMultiple(long l1, long l2) {
		long largest = l1 < l2 ? l2 : l1;
		long current = largest;
		
		while (current % l1 != 0 || current % l2 != 0) {
			current = current + largest;
		}
		return current;
	}
}
