package darq.math.geometry;

import darq.math.Const;
import darq.math.Utils;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
	
	private static final Point[] cornerPoints = new Point[] {
		staticGetDeltaInDirection( 2,  1, 2D / 3D),	// Middle of hexant 0.
		staticGetDeltaInDirection( 1,  2, 2D / 3D),	// Middle of hexant 1.
		staticGetDeltaInDirection(-1,  1, 2D / 3D),	// Middle of hexant 2.
		staticGetDeltaInDirection(-2, -1, 2D / 3D),	// Middle of hexant 3.
		staticGetDeltaInDirection(-1, -2, 2D / 3D),	// Middle of hexant 4.
		staticGetDeltaInDirection( 1, -1, 2D / 3D)	// Middle of hexant 5.	
	};
	
	public static double staticDistance(double yD, double xD) {
		if (Utils.sign(yD) * Utils.sign(xD) < 0) {
			return Math.abs(yD) + Math.abs(xD);
		} else {
			return Math.max(Math.abs(yD), Math.abs(xD));
		}
	}

	@Override
	public double distance(double yD, double xD) {
		if (Utils.sign(yD) * Utils.sign(xD) < 0) {
			return Math.abs(yD) + Math.abs(xD);
		} else {
			return Math.max(Math.abs(yD), Math.abs(xD));
		}
	}
	
	public static Coord round(double yD, double xD, boolean conservative, int direction) {
		Coord[] coords = round(yD, xD);
		return specRound(coords, conservative, direction);
	}
	
	public static Coord[] round(double yD, double xD) {
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
		if (Utils.equals(staticDistance(yD, xD) % 1, 0.5) && (Utils.equals(zE, 0) || Utils.equals(yE, 0) || Utils.equals(xE, 0))) {
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
	
	private static Coord specRound(Coord[] coords, boolean conservative, int direction) {
		Coord value = coords[0];
		for (int i = 1; i < coords.length; i++) {
			Coord coord = coords[i];
			if (coord == null) {
				continue;
			}
			
			double distanceD = staticDistance(value.y, value.x) - staticDistance(coord.y, coord.x);
			if (!Utils.equals(distanceD, 0)) {
				distanceD = distanceD * (conservative ? -1 : 1);
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
	
	private static class RoundComparator implements Comparator<Coord> {
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
			
			delta = staticDistance(o1.y, o1.x) - staticDistance(o2.y, o2.x);
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
	
	public static double hexant(double yD, double xD) {
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
	
	public static double hexantCompare(double y1, double x1, double y2, double x2, int direction, boolean circle) {
		double delta = (hexant(y2, x2) - hexant(y1, x1)) * direction;
		if (circle && (delta > 3 || delta <= -3)) {
			delta = delta - (6 * Utils.sign(delta));
		}
		return delta;
	}
	
	public static Point staticGetDeltaInDirection(double yD, double xD, double step) {
		double reference = staticDistance(yD, xD);
		double ratio = step / reference;
		return new Point(yD * ratio, xD * ratio);
	}
	
	public static Point getDeltaInArc(double yD, double xD, double step) {
		double distance = staticDistance(yD, xD);
		double hexantStep = step / distance;
		double hexant = Utils.modulus(hexant(yD, xD) + hexantStep, 6);
		return getPointByHexant(distance, hexant);
	}
	
	public static Point getPointByHexant(double distance, double hexant) {
		int hexantIndex = (int) hexant;
		Coord start = hexantStart[hexantIndex];
		Coord forward = hexantForward[hexantIndex];
		
		double hexantDelta = hexant - hexantIndex;
		double y = (start.y + (forward.y * hexantDelta)) * distance;
		double x = (start.x + (forward.x * hexantDelta)) * distance;
		
		return new Point(y, x);
	}
	
	public static Point[] getLine(double y1, double x1, double y2, double x2, double step) {
		int size = (int) Math.ceil(staticDistance(y2 - y1, x2 - x1) / step);
		return getLine(y1, x1, y2, x2, step, size);
	}
	
	public static Point[] getLine(double y1, double x1, double y2, double x2, double step, int size) {		
		double yD = y2 - y1;
		double xD = x2 - x1;
		
		Point steps = staticGetDeltaInDirection(yD, xD, step);
		
		return getLine(y1, x1, steps.y, steps.x, size);
	}
	
	public static Point[] getLine(double y1, double x1, double yS, double xS, int size) {
		// Initialise the line.
		Point[] line = new Point[size];
		
		// Populate the line.
		line[0] = new Point(y1, x1);
		for (int i = 1; i < size; i++) {
			y1 = y1 + yS;
			x1 = x1 + xS;
			line[i] = new Point(y1, x1);
		}
		
		return line;
	}
	
	public static List<Coord> getBurst(int yS, int xS, int radius, CoordCheckFunctor pass) {
		List<Coord> burst = new LinkedList<Coord>();
		
		getCone(yS, xS, yS + radius, xS, yS - radius, xS, DIRECTION_CLOCKWISE, 1, radius, pass, burst);
		getCone(yS, xS, yS - radius, xS, yS + radius, xS, DIRECTION_CLOCKWISE, 1, radius, pass, burst);
		
		return burst;
	}
	
	public static List<Coord> getCone(int yS, int xS, double y1, double x1, double y2, double x2, int direction, int distance, int endDistance, CoordCheckFunctor pass) {
		List<Coord> cone = new LinkedList<Coord>();
		
		getCone(yS, xS, y1, x1, y2, x2, direction, distance, endDistance, pass, cone);
		
		return cone;
	}
	
	private static void getCone(int yS, int xS, double y1, double x1, double y2, double x2, int direction, int distance, int endDistance, CoordCheckFunctor pass, List<Coord> cone) {
		Point[] line1 = getLine(yS, xS, y1, x1, 1, endDistance + 1);
		Point[] line2 = getLine(yS, xS, y2, x2, 1, endDistance + 1);
		getConeSymmetric(yS, xS, line1, line2, direction, distance, pass, cone);
//		getConeAsymmetric(yS, xS, line1, line2, direction, staticDistance, pass, cone);
	}
	
	// TODO: Seems to be buggy, possibly due to rounding errors.
	// Proof: Wall directly south from source, at staticDistance 5, but cannot reproduce reliably.
	private static void getConeSymmetric(int yS, int xS, Point[] line1, Point[] line2, int direction, int distance, CoordCheckFunctor pass, List<Coord> cone) {		
		// Points delimiting the arc.
		Point point1 = line1[distance];
		Point point2 = line2[distance];
		
		// To ensure symmetry, use only delta.
		point1 = new Point(point1.y - yS, point1.x - xS);
		point2 = new Point(point2.y - yS, point2.x - xS);
		
		// To allow walls to be seen at any point, extend the arc.
		Point extPoint1 = getDeltaInArc(point1.y, point1.x, -direction);
		Point extPoint2 = getDeltaInArc(point2.y, point2.x, direction);
		
		// Round the Points to Coords.
		Coord coord1 = specRound(round(extPoint1.y, extPoint1.x), false, -direction);
		Coord coord2 = specRound(round(extPoint2.y, extPoint2.x), false, direction);
				
		// Obtain the arc between the two coords.
		List<Coord> arc = getArc(yS, xS, coord1.y + yS, coord1.x + xS, coord2.y + yS, coord2.x + xS, direction);
		
		// Only add tiles if their centres are visible, check first and last.
		while (!arc.isEmpty()) {
			coord1 = arc.get(0);
			coord1 = new Coord(coord1.y - yS, coord1.x - xS);
			
			// If centre is visible, resume normal arc processing.
			double delta = hexantCompare(point1.y, point1.x, coord1.y, coord1.x, direction, true);
			if (delta >= -Const.EPSILON) {
				break;
			}
			
			// If any part is visible and the tile is blocking.
			if (!pass.check(coord1.y + yS, coord1.x + xS)) {
				Point criticalPoint = getCriticalPoints(coord1.y, coord1.x, -direction)[0];
				delta = hexantCompare(point1.y, point1.x, coord1.y + criticalPoint.y, coord1.x + criticalPoint.x, direction, true);
				if (delta >= -Const.EPSILON) {
					break;
				}
			}
			
			arc.remove(0);
		}
		while (!arc.isEmpty()) {
			coord2 = arc.get(arc.size() - 1);
			coord2 = new Coord(coord2.y - yS, coord2.x - xS);
			
			// If centre is visible, resume normal arc processing.
			double delta = hexantCompare(point2.y, point2.x, coord2.y, coord2.x, -direction, true);
			if (delta >= -Const.EPSILON) {
				break;
			}
			
			// If any part is visible and the tile is blocking.
			if (!pass.check(coord2.y + yS, coord2.x + xS)) {
				Point criticalPoint = getCriticalPoints(coord2.y, coord2.x, direction)[0];
				delta = hexantCompare(point2.y, point2.x, coord2.y + criticalPoint.y, coord2.x + criticalPoint.x, -direction, true);
				if (delta >= -Const.EPSILON) {
					break;
				}
			}
			
			arc.remove(arc.size() - 1);
		}
		
		// Add all tiles at this staticDistance to the cone.
		cone.addAll(arc);

		// If not at edge of radius, algorithm may need recursing.
		if (distance < line1.length -1) {
			if (arc.isEmpty()) {
				// Recurse, as the return skips the last recursion.
//				getConeSymmetric(yS, xS, line1, line2, direction, staticDistance + 1, pass, cone);
				return;
			}
			
			Iterator<Coord> iterator = arc.iterator();
			// Arc has at least one hex, first hex cannot recurse.
			Coord coord = iterator.next();
			boolean blocking = !pass.check(coord.y, coord.x);
			Coord previous = coord;
			while (iterator.hasNext()) {
				coord = iterator.next();
				boolean blocks = !pass.check(coord.y, coord.x);

				// If blocking starts.
				if (!blocking && blocks) {
					blocking = true;
					
					// Update the second line.
					Point lastClearPoint = getCriticalPoints(coord.y - yS, coord.x - xS, direction)[0];
					Point[] line3 = getLine(yS, xS, coord.y + lastClearPoint.y, coord.x + lastClearPoint.x, 1, line2.length);
					
					// Recurse.
//					System.out.println("\t\tRecursing to staticDistance " + (staticDistance + 1));
					getConeSymmetric(yS, xS, line1, line3, direction, distance + 1, pass, cone);
				}

				// If blocking ends.
				if (blocking && !blocks) {
					blocking = false;
					
					// Update the first line, but do not recurse yet.
					// Recursion will be done when a new block is found.
					Point firstClearPoint = getCriticalPoints(previous.y - yS, previous.x - xS, direction)[1];
					line1 = getLine(yS, xS, previous.y + firstClearPoint.y, previous.x + firstClearPoint.x, 1, line1.length);
				}

				previous = coord;
			}

			// If the last tile is open, need to recurse one last time.
			if (!blocking) {
				getConeSymmetric(yS, xS, line1, line2, direction, distance + 1, pass, cone);
			}
		}
	}
	
	// TODO: Seems to be buggy, possibly due to rounding errors.
	// Proof: Wall directly south from source, at staticDistance 5, but cannot reproduce reliably.
	private static void getConeAsymmetric(int yS, int xS, Point[] line1, Point[] line2, int direction, int distance, CoordCheckFunctor pass, List<Coord> cone) {		
		// Points delimiting the arc.
		Point point1 = line1[distance];
		Point point2 = line2[distance];
		
		// To ensure symmetry, use only delta.
		point1 = new Point(point1.y - yS, point1.x - xS);
		point2 = new Point(point2.y - yS, point2.x - xS);
		
		if (hexantCompare(point1.y, point1.x, point2.y, point2.x, direction, true) < 0) {
			return;
		}
		
		// Round the Points to Coords.
		Coord coord1 = specRound(round(point1.y, point1.x), false, -direction);
		Coord coord2 = specRound(round(point2.y, point2.x), false, direction);
		
//		System.out.print("Distance: " + staticDistance + ". Points: " + point1 + " and " + point2 + ". Coords: " + coord1 + " and " + coord2 + ".");
		List<Coord> arc = getArc(yS, xS, coord1.y + yS, coord1.x + xS, coord2.y + yS, coord2.x + xS, direction);
		
		// Add all tiles at this staticDistance to the cone.
		cone.addAll(arc);

		// If not at edge of radius, algorithm may need recursing.
		if (distance < line1.length -1) {
			Iterator<Coord> iterator = arc.iterator();
			// Arc has at least one hex, first hex cannot recurse.
			Coord coord = iterator.next();
			boolean blocking = !pass.check(coord.y, coord.x);
			Coord previous = coord;
			while (iterator.hasNext()) {
				coord = iterator.next();
				boolean blocks = !pass.check(coord.y, coord.x);

				// If blocking starts.
				if (!blocking && blocks) {
					blocking = true;
					
					// Update the second line.
					Point lastClearPoint = getCriticalPoints(coord.y - yS, coord.x - xS, direction)[0];
					Point[] line3 = getLine(yS, xS, coord.y + lastClearPoint.y, coord.x + lastClearPoint.x, 1, line2.length);
					
					// Recurse.
//					System.out.println("\t\tRecursing to staticDistance " + (staticDistance + 1));
					getConeAsymmetric(yS, xS, line1, line3, direction, distance + 1, pass, cone);
				}

				// If blocking ends.
				if (blocking && !blocks) {
					blocking = false;
					
					// Update the first line, but do not recurse yet.
					// Recursion will be done when a new block is found.
					Point firstClearPoint = getCriticalPoints(previous.y - yS, previous.x - xS, direction)[1];
					line1 = getLine(yS, xS, previous.y + firstClearPoint.y, previous.x + firstClearPoint.x, 1, line1.length);
				}

				previous = coord;
			}

			// If the last tile is open, need to recurse one last time.
			if (!blocking) {
				getConeAsymmetric(yS, xS, line1, line2, direction, distance + 1, pass, cone);
			}
		}
	}
	
	public static List<Coord> getArc(int y0, int x0, int y1, int x1, int y2, int x2, int direction) {
		List<Coord> arc = new LinkedList<Coord>();
		
		int radius = (int) staticDistance(y1 - y0, x1 - x0);
		if ((int) staticDistance(y2 - y0, x2 - x0) != radius) {
			throw new IllegalArgumentException("Starting from (" + y0 + ", " + x0 + "), coords (" + y1 + ", " + x1 + ") and (" + y2 + ", " + x2 + ") do not form an arc.");
		}
		
		int hexant = (int) hexant(y1 - y0, x1 - x0);
		int yDir = hexantForward[hexant].y * direction;
		int xDir = hexantForward[hexant].x * direction;
		int corner = direction < 0 ? hexant : ((hexant + 1) % 6);
		int yEnd = y0 + hexantStart[corner].y * radius;
		int xEnd = x0 + hexantStart[corner].x * radius;
		
		arc.add(new Coord(y1, x1));
		while (y1 != y2 || x1 != x2) {
			y1 += yDir;
			x1 += xDir;
			
			arc.add(new Coord(y1, x1));
			
			if (y1 == yEnd && x1 == xEnd) {
				hexant = (int) Utils.modulus(hexant + direction, 6);
				yDir = hexantForward[hexant].y * direction;
				xDir = hexantForward[hexant].x * direction;
				corner = (int) Utils.modulus(corner + direction, 6);
				yEnd = y0 + hexantStart[corner].y * radius;
				xEnd = x0 + hexantStart[corner].x * radius;
			}
		}
		
		return arc;
	}
	
	private static Point[] getCriticalPoints(double yD, double xD, int direction) {
		double hexant = hexant(yD, xD);
		int hexantIndex = (int) hexant;
		double hexantDelta = hexant - hexantIndex;
		
		// The staticDistance from closest integer hexant at which a point (yD, xD)
		// becomes able to see a point of the hexagon (0, 0).
		double threshold = (1 - (1 / staticDistance(yD, xD))) / 2;
		
		int negOffset = 1;
		int posOffset = 1;
		if (hexantDelta > threshold) {
			posOffset = 2;
		}
		if ((1 - hexantDelta) > threshold) {
			negOffset = 2;
		}

		int p1;
		int p2;
		if (direction == DIRECTION_CLOCKWISE) {
			p1 = (int) Utils.modulus(hexantIndex - negOffset, 6);
			p2 = (int) Utils.modulus(hexantIndex + posOffset, 6);
		} else {
			p1 = (int) Utils.modulus(hexantIndex + posOffset, 6);
			p2 = (int) Utils.modulus(hexantIndex - negOffset, 6);
		}
		
		return new Point[] {cornerPoints[p1], cornerPoints[p2]};
	}
	
	public static Coord[] getNeighbours(Coord coord) {
		Coord[] neighbours = new Coord[6];
		neighbours[0] = new Coord(coord.y + 1, coord.x);
		neighbours[1] = new Coord(coord.y + 1, coord.x + 1);
		neighbours[2] = new Coord(coord.y, coord.x + 1);
		neighbours[3] = new Coord(coord.y - 1, coord.x);
		neighbours[4] = new Coord(coord.y - 1, coord.x - 1);
		neighbours[5] = new Coord(coord.y, coord.x - 1);
		return neighbours;
	}
}