package darq.math;

import darq.math.geometry.Point;

/**
 *
 * @author Craig.Webster
 */
public class Utils {
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards positive infinity.
	 * @param value
	 * @return 
	 */
	public static long roundOpt(double value) {
		return (long) Math.floor(value + 0.5);
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards positive infinity.
	 * This method employs <code>Const.EPSILON</code> to determine ties.
	 * @param value
	 * @return 
	 */
	public static long roundOptEps(double value) {
		return (long) Math.floor(value + 0.5 + Const.EPSILON);
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards infinity of the same sign as the value.
	 * For example, 1.5 rounds to 2, -1.5 rounds to -2.
	 * @param value
	 * @return 
	 */
	public static long roundOptSym(double value) {
		if (value >= 0) {
			return roundOpt(value);
		} else {
			return roundPes(value);
		}
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards infinity of the same sign as the value.
	 * This method employs <code>Const.EPSILON</code> to determine ties.
	 * @param value
	 * @return 
	 */
	public static long roundOptSymEps(double value) {
		if (value >= 0) {
			return roundOptEps(value);
		} else {
			return roundPesEps(value);
		}
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards negative infinity.
	 * @param value
	 * @return 
	 */
	public static long roundPes(double value) {
		return (long) Math.ceil(value - 0.5);
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards negative infinity.
	 * This method employs <code>Const.EPSILON</code> to determine .5 values.
	 * @param value
	 * @return 
	 */
	public static long roundPesEps(double value) {
		return (long) Math.ceil(value - 0.5 - Const.EPSILON);
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards zero.
	 * For example, 1.5 rounds to 1, -1.5 rounds to -1.
	 * @param value
	 * @return 
	 */
	public static long roundPesSym(double value) {
		if (value >= 0) {
			return roundPes(value);
		} else {
			return roundOpt(value);
		}
	}
	
	/**
	 * Returns the closest <code>long</code> to the value,
	 * with ties rounding towards zero.
	 * For example, 1.5 rounds to 1, -1.5 rounds to -1.
	 * This method employs <code>Const.EPSILON</code> to determine ties.
	 * @param value
	 * @return 
	 */
	public static long roundPesSymEps(double value) {
		if (value >= 0) {
			return roundPesEps(value);
		} else {
			return roundOptEps(value);
		}
	}
	
	/**
	 * Returns the value in values that is closest to negative infinity.
	 * Returns <code>Double.POSITIVE_INFINITY</code> if no values are supplied.
	 * @param values
	 * @return 
	 */
	public static double min(double... values) {
		double min = Double.POSITIVE_INFINITY;
		for (double value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
	
	/**
	 * Returns the value in values that is closest to positive infinity.
	 * Returns <code>Double.NEGATIVE_INFINITY</code> if no values are supplied.
	 * @param values
	 * @return 
	 */
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
		if (equals(value, 0)) {
			return 0;
		} else {
			return (int) (value / Math.abs(value));
		}
	}
	
	public static double closest(double reference, double doubleOne, double doubleTwo) {
		return Math.abs(reference - doubleOne) <= Math.abs(reference - doubleTwo) ? doubleOne : doubleTwo;
	}
	
	public static double closest(double reference, double... values) {
		double closest = Double.NaN;
		double distance = Double.POSITIVE_INFINITY;
		for (double value : values) {
			double temp = Math.abs(reference - value);
			if (temp < distance) {
				closest = value;
				distance = temp;
			}
		}
		return closest;
	}
	
	/**
	 * Returns true if values are less than <code>Const.EPSILON</code> apart.
	 * Also returns true if both values are equal to Double.NaN,
	 * Double.POSITIVE_INFINITY or Double.NEGATIVE_INFINITY.
	 * Returns false otherwise.
	 * @param value1
	 * @param value2
	 * @return 
	 */
	public static boolean equals(double value1, double value2) {
		if ((Double.isNaN(value1) && Double.isNaN(value2)) || (value1 == Double.POSITIVE_INFINITY && value2 == Double.POSITIVE_INFINITY) || (value1 == Double.NEGATIVE_INFINITY && value2 == Double.NEGATIVE_INFINITY)) {
			return true;
		}
		return (Math.abs(value1 - value2) < Const.EPSILON);
	}
	
	/**
	 * Returns true if d1 is less than but not equal to d2.
	 * Returns false otherwise.
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static boolean lt(double d1, double d2) {
		return d1 < d2 && !equals(d1, d2);
	}
	
	/**
	 * Returns true if d1 is less than or equal to d2.
	 * Returns false otherwise.
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static boolean lte(double d1, double d2) {
		return d1 <= d2 || equals(d1, d2);
	}
	
	/**
	 * Returns true if d1 is greater than but not equal to d2.
	 * Returns false otherwise.
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static boolean gt(double d1, double d2) {
		return d1 > d2 && !equals(d1, d2);
	}
	
	/**
	 * Returns true if d1 is greater than or equal to d2.
	 * Returns false otherwise.
	 * @param d1
	 * @param d2
	 * @return 
	 */
	public static boolean gte(double d1, double d2) {
		return d1 >= d2 || equals(d1, d2);
	}
	
	public static int compare(double d1, double d2) {
		return sign(d1 - d2);
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
	
	public static long summation(long from, long to, long step) {
		if ((to - from) * step < 0) {
			throw new ArithmeticException("Summation from " + from + " to " + to + ", with step " + step + ", will never terminate.");
		}
		long sum = 0;
		for (long i = from; i <= to; i += step) {
			sum += i;
		}
		return sum;
	}
	
	public static double summation(double from, double to, double step) {
		if ((to - from) * step < 0) {
			throw new ArithmeticException("Summation from " + from + " to " + to + ", with step " + step + ", will never terminate.");
		}
		double sum = 0;
		for (double d = from; lte(d, to); d += step) {
			sum += d;
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
