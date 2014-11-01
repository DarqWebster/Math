package darq.math.geometry;

import darq.math.Const;
import darq.math.Utils;
import darq.math.exception.MathException;

/**
 *
 * @author craig
 */
public class Vector {
	public enum Direction {
		Y_INC,
		Y_DEC,
		X_INC,
		X_DEC;
	}
	
	public final Line line;
	public final int yDirection;
	public final int xDirection;
	public final double yVector;
	public final double xVector;
	
	public Vector(Line line, Direction direction) throws MathException {
		this.line = line;
		
		int tempYDirection;
		int tempXDirection;
		double gradient = line.getGradient();
		if (gradient > 0) {
			if (direction == Direction.Y_INC || direction == Direction.X_INC) {
				tempYDirection = +1;
				tempXDirection = +1;
			} else {
				tempYDirection = -1;
				tempXDirection = -1;
			}
		} else {
			if (direction == Direction.Y_INC || direction == Direction.X_DEC) {
				tempYDirection = +1;
				tempXDirection = -1;
			} else {
				tempYDirection = -1;
				tempXDirection = +1;
			}
		}

		if (Utils.equals(gradient, 0)) {
			if (direction == Direction.Y_INC || direction == Direction.Y_DEC) {
				throw new MathException("A Vector with a horizontal gradient cannot be declared with the y value increasing or decreasing.");
			}
			tempYDirection = 0;
		}
		if (Utils.equals(gradient, Const.VERTICAL_GRADIENT)) {
			if (direction == Direction.X_INC || direction == Direction.X_DEC) {
				throw new MathException("A Vector with a vertical gradient cannot be declared with the x value increasing or decreasing.");
			}
			tempXDirection = 0;
		}
		
		yDirection = tempYDirection;
		xDirection = tempXDirection;
		
		double length = Math.sqrt(Math.pow(line.yCo, 2) + Math.pow(line.xCo, 2));
		yVector = line.yCo / length;
		xVector = -line.xCo / length;
	}
}
