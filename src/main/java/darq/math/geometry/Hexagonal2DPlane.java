package darq.math.geometry;

import darq.math.Utils;
import java.util.Comparator;

/**
 * The following are defined as relative to a particular point.
 * 
 * Hexants:
 *     0            0   1
 * 5       1
 *     *      ||  5   *   2
 * 4       2
 *     3            4   3
 * 
 * y increases in the direction of 0 and decreases in the direction of 3.
 * x increases in the direction of 2 and decreases in the direction of 5.
 * Both increase in the direction of 1 and decrease in the direction of 4.
 * Visualised:
 *     +,0          +,0 +,+
 * 0,-     +,+
 *      *      || 0,-  *  0,+
 * -,-     0,+
 *     -,0          -,- -,0
 * 
 * Hexant positions:
 *         0                  0   1   0
 *     1       1                        
 * 0       0       0        1   0   0   1
 *     0       0                          
 * 1       *       1  ||  0   0   *   0   0
 *     0       0                          
 * 0       0       0        1   0   0   1
 *     1       1                        
 *         0                  0   1   0
 * Hexes are defined by a radius, a hexant, and a position.
 * 
 * Circle positions:
 *         0                  0   1   2
 *     11      1                        
 * 10      0       2        11  0   1   3
 *     5       1                          
 * 9       *       3  ||  10  5   *   2   4
 *     4       2                          
 * 8       3       4        9   4   3   5
 *     7       5                        
 *         6                  8   7   6
 * Hexes are defined by a radius and a position.
 * 
 * Spiral positions:
 *         7                  7   8   9
 *     18      8                        
 * 17      1       9        18  1   2   10
 *     6       2                          
 * 16      0       10 ||  17  6   0   3   11
 *     5       3                          
 * 15      4       11       16  5   4   12
 *     14      12                        
 *         13                 15  14  13
 * Hexes are defined by a position.
 * 
 * 
 * @author Craig.Webster
 */
public class Hexagonal2DPlane extends Abstract2DPlane {
	public static final int ROUND_UP = 1;
	public static final int ROUND_DOWN = -1;
	public static final int DIRECTION_CLOCKWISE = 1;
	public static final int DIRECTION_ANTICLOCKWISE = -1;
	
	private static final Coord[] hexantStart = {
		new Coord(+1,  0),
		new Coord(+1, +1),
		new Coord( 0, +1),
		new Coord(-1,  0),
		new Coord(-1, -1),
		new Coord( 0, -1)
	};
	
	private static final Coord[] hexantForward = {
		new Coord( 0, +1),
		new Coord(-1,  0),
		new Coord(-1, -1),
		new Coord( 0, -1),
		new Coord(+1,  0),
		new Coord(+1, +1)
	};

	@Override
	public double distance(double yD, double xD) {
		if (Utils.sign(yD) * Utils.sign(xD) < 0) {
			return Math.abs(yD) + Math.abs(xD);
		} else {
			return Math.max(Math.abs(yD), Math.abs(xD));
		}
	}
	
	public Coord round(double yD, double xD, int round, int direction) {
		Coord[] coords = round(yD, xD);
		return specRound(coords, round, direction);
	}
	
	public Coord[] round(double yD, double xD) {
		// Obtain the third axis.
		double zD = yD - xD;
		
		// Rounded values.
		int zR = (int) Utils.roundPesSymEps(zD);
		int yR = (int) Utils.roundPesSymEps(yD);
		int xR = (int) Utils.roundPesSymEps(xD);
		
		// Error margins.
		double zE = Math.abs(zD - zR);
		double yE = Math.abs(yD - yR);
		double xE = Math.abs(xD - xR);
				
		// Degenerate case where point is on an edge and 0.5 away from centre.
		if (Utils.equals(distance(yD, xD) % 1, 0.5) && (Utils.equals(zE, 0) || Utils.equals(yE, 0) || Utils.equals(xE, 0))) {
			return new Coord[] {
				new Coord(yR, xR),
				new Coord((int) Utils.roundOptSymEps(yD), (int) Utils.roundOptSymEps(xD))
			};
		}
		
		// If all is in balance, don't bother.
		if (zR - yR + xR == 0) {
			return new Coord[] {new Coord(yR, xR)};
		}
		
		// Discard largest components and reconstruct from z - y + x = 0;
		double max = Utils.max(zE, yE, xE);
		Coord[] prelims = new Coord[3];
		int count = 0;
		if (Utils.equals(zE, max)) {
			// Discard Z.
			prelims[count++] = new Coord(yR, xR);
		}
		if (Utils.equals(yE, max)) {
			// Discard Y.
			prelims[count++] = new Coord((zR + xR), xR);
		}
		if (Utils.equals(xE, max)) {
			// Discard X.
			prelims[count++] = new Coord(yR, (-zR + yR));
		}
		
		// Sort network.
		// No need for a full sort algorithm.
		RoundComparator comparator = new RoundComparator();
		if (comparator.compare(prelims[1], prelims[2]) > 0) {
			Coord temp = prelims[1];
			prelims[1] = prelims[2];
			prelims[2] = temp;
		}
		if (comparator.compare(prelims[0], prelims[2]) > 0) {
			Coord temp = prelims[0];
			prelims[0] = prelims[2];
			prelims[2] = temp;
		}
		if (comparator.compare(prelims[0], prelims[1]) > 0) {
			Coord temp = prelims[0];
			prelims[0] = prelims[1];
			prelims[1] = temp;
		}
		
		// Trim array to contents;
		Coord[] results = new Coord[count];
		for (int i = 0; i < count; i++) {
			results[i] = prelims[i];
		}
		
		return results;
	}
	
	private Coord specRound(Coord[] coords, int round, int direction) {
		Coord value = coords[0];
		for (int i = 1; i < coords.length; i++) {
			Coord coord = coords[i];
			if (coord == null) {
				continue;
			}
			
			double distanceD = distance(value.y, value.x) - distance(coord.y, coord.x);
			if (!Utils.equals(distanceD, 0)) {
				distanceD = distanceD * round;
				if (distanceD < 0) {
					value = coord;
				}
			} else {
				double hexantD = hexantCompare(coord.y, coord.x, value.y, value.x, direction, true);
				if (!Utils.equals(hexantD, 0) && hexantD < 0) {
					value = coord;
				}
			}
		}
		
		return value;
	}
	
	private class RoundComparator implements Comparator<Coord> {
		/**
		 * Compares two Coords, based on nullity, then distance, then hexants.
		 * Returns (-)3 if one Coord is null, nulls sort to the end.
		 * Returns (-)2 if one Coord is further away than the other.
		 * Returns (-)1 if one Coord is circularly before the other.
		 * Returns 0 if both parameters are equal.
		 * 
		 * @param o1
		 * @param o2
		 * @return 
		 */
		@Override
		public int compare(Coord o1, Coord o2) {
			if (o1 == null && o2 == null) { return  0; }
			if (o1 == null && o2 != null) { return  3; }
			if (o1 != null && o2 == null) { return -3; }
			
			double delta;
			
			delta = distance(o1.y, o1.x) - distance(o2.y, o2.x);
			if (!Utils.equals(delta, 0)) {
				return Utils.sign(delta) * 2;
			}
			
			delta = hexant(o1.y, o1.x) - hexant(o2.y, o2.x);

			// Use "shortest" circular distance between Coords.
			// Example, hexant 5 is circularly before hexant 0.
			// Does not preserve actual delta between Coords.
			if (Math.abs(delta) > 3) {
				delta = -delta;
			}
			if (!Utils.equals(delta, 0)) {
				return Utils.sign(delta);
			}
			
			return 0;
		}
	}
	
	public double hexant(double yD, double xD) {
		if (yD > 0 && xD >= 0 && yD > xD) {
			return 0 + (xD / yD);
		}
		if (yD > 0 && xD > 0 && yD <= xD) {
			return 1 + (1 - (yD / xD));
		}
		if (yD <= 0 && xD > 0) {
			return 2 + (yD / (yD - xD));
		}
		if (yD < 0 && xD <= 0 && yD < xD) {
			return 3 + (xD / yD);
		}
		if (yD < 0 && xD < 0 && yD >= xD) {
			return 4 + (1 - (yD / xD));
		}
		if (yD >= 0 && xD < 0) {
			return 5 + (yD / (yD - xD));
		}
		
		return -1;
	}
	
	public double hexantCompare(double y1, double x1, double y2, double x2, int direction, boolean circle) {
		double delta = (hexant(y2, x2) - hexant(y1, x1)) * direction;
		if (circle && (delta > 3 || delta <= -3)) {
			delta = delta - (6 * Utils.sign(delta));
		}
		return delta;
	}
	
	public Point getDeltaInArc(double yD, double xD, double step) {
		double distance = distance(yD, xD);
		double hexantStep = step / distance;
		double hexant = Utils.modulus(hexant(yD, xD) + hexantStep, 6);
		return getPointByHexant(distance, hexant);
	}
	
	public Point getPointByHexant(double distance, double hexant) {
		int hexantIndex = (int) hexant;
		Coord start = hexantStart[hexantIndex];
		Coord forward = hexantForward[hexantIndex];
		
		double hexantDelta = hexant - hexantIndex;
		double y = (start.y + (forward.y * hexantDelta)) * distance;
		double x = (start.x + (forward.x * hexantDelta)) * distance;
		
		return new Point(y, x);
	}
	
	public Coord getHexantStart(int hexant) {
		return hexantStart[hexant];
	}
	
	public Coord getHexantForward(int hexant) {
		return hexantStart[hexant];
	}
}