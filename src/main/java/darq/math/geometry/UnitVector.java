package darq.math.geometry;

import darq.math.Const;
import darq.math.Utils;
import darq.math.exception.MathException;

/**
 *
 * @author craig
 */
public class UnitVector {
	public enum Direction {
		Y_INC,
		Y_DEC,
		X_INC,
		X_DEC;
	}
	
	public final double yVector;
	public final double xVector;
	
	public UnitVector(double gradient, Direction direction) throws MathException {
		// Handle simple or degenerate cases;
		if (Utils.equals(gradient, 0)) {
			if (direction == Direction.Y_INC || direction == Direction.Y_DEC) {
				throw new MathException("A Vector with a horizontal gradient cannot be declared with the y value increasing or decreasing.");
			}
			yVector = 0;
			xVector = 1;
			return;
		}
		if (Utils.equals(gradient, Const.VERTICAL_GRADIENT)) {
			if (direction == Direction.X_INC || direction == Direction.X_DEC) {
				throw new MathException("A Vector with a vertical gradient cannot be declared with the x value increasing or decreasing.");
			}
			yVector = 1;
			xVector = 0;
			return;
		}
		
		int yDirection;
		int xDirection;
		if (gradient > 0) {
			if (direction == Direction.Y_INC || direction == Direction.X_INC) {
				yDirection = +1;
				xDirection = +1;
			} else {
				yDirection = -1;
				xDirection = -1;
			}
		} else {
			if (direction == Direction.Y_INC || direction == Direction.X_DEC) {
				yDirection = +1;
				xDirection = -1;
			} else {
				yDirection = -1;
				xDirection = +1;
			}
		}
		
		// gradient == delta y / delta x;
		// therefore allow:
		//     delta y == gradient;
		//     delta x == 1;
		//
		// distance between two points == (((delta y)^2) + ((delta x)^2))^(1/2)
		//                             == (gradient^2 + 1)^(1/2)
		double r = Math.sqrt(gradient * gradient + 1);
		yVector = (gradient / r) * yDirection;
		xVector = (1 / r) * xDirection;
	}
	
	public Point getPointAtDistance(Point point, double magnitude) {
		return new Point(point.y + (magnitude * yVector), point.x + (magnitude * xVector));
	}

	@Override
	public String toString() {
		return "DirectionVector{" + "yVector=" + yVector + ", xVector=" + xVector + '}';
	}
}
