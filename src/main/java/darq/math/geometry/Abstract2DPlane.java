package darq.math.geometry;

import darq.math.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collector;

/**
 *
 * @author Craig.Webster
 */
public abstract class Abstract2DPlane {
	/**
	 * Returns the length of the line drawn from the origin, to the given point.
	 * The line is drawn from (0, 0) to (yD, xD).
	 * To be completed by the subclass.
	 * @param yD
	 * @param xD
	 * @return The length of the line drawn from (0, 0) to (yD, xD).
	 */
	public abstract double distance(double yD, double xD);
	
	/**
	 * Calculate the distance between the given points.
	 * Uses the subclass's implementation of <code>distance(double, double)</code>.
	 * @param p1
	 * @param p2
	 * @return 
	 */
	public double distance(Point p1, Point p2) {
		return distance(p2.y - p1.y, p2.x - p1.x);
	}
	
	/**
	 * Calculate the angle from "North" to the line defined by the given deltas.
	 * The returned value ranges from 0 to 2 * PI radians.
	 * 
	 * For example, the angle A below is greater than PI / 2 and less than PI.
	 *         |
	 *         |__
	 *         |   \
	 * ----------A------
	 *         |\_ /
	 *         | \
	 *         |  \
	 * 
	 * @param yD
	 * @param xD
	 * @return The angle, in radians, between "north" and the given line.
	 */
	public double angle(double yD, double xD) {
		if (Utils.equals(yD, 0) && Utils.equals(xD, 0)) {
			return Double.NaN;
		}
		double result = Math.atan2(xD, yD);
		if (result < 0) {
			result = 2 * Math.PI + result;
		}
		return result;
	}
	
	public double angle(Point from, Point to) {
		return angle(to.y - from.y, to.x - from.x);
	}
	
	/**
	 * Move the given Point by the given deltas.
	 * @param point
	 * @param yD
	 * @param xD
	 * @return The given Point moved yD on the y-axis and xD on the x-axis.
	 */
	public Point adjust(Point point, double yD, double xD) {
		return new Point(point.y + yD, point.x + xD);
	}
	
	/**
	 * Move the given Segment by the given deltas.
	 * @param segment
	 * @param yD
	 * @param xD
	 * @return The given Segment moved yD on the y-axis and xD on the x-axis.
	 */
	public Segment adjust(Segment segment, double yD, double xD) {
		return new Segment(adjust(segment.pS, yD, xD), adjust(segment.pE, yD, xD));
	}
	
	public Polygon adjust(Polygon polygon, double yD, double xD) {
		return new Polygon(polygon.points.stream().collect(Collector.of(
				// Collector<Point, ?, Point[]>;
				() -> new ArrayList<Point>(polygon.points.size()),
				(a, p) -> a.add(adjust(p, yD, xD)),
				(a1, a2) -> {
					a1.addAll(a2);
					return a1;
				},
				(a) -> a.toArray(new Point[polygon.points.size()])
		)));
	}
	
	/**
	 * Calculate the Point,
	 * <code>step</code> distance along the line defined by the given deltas.
	 * @param yD
	 * @param xD
	 * @param step
	 * @return The Point along the line defined by the given Points, at the distance defined by step.
	 */
	public Point getDeltaInDirection(double yD, double xD, double step) {
		double reference = distance(yD, xD);
		double ratio = step / reference;
		return new Point(yD * ratio, xD * ratio);
	}
	
	/**
	 * Calculates where the given Point is, relative to the given Segment.
	 * The Segment defined a line infinitely long in both directions.
	 * Terms "left" and "right" are defined relative to the Segment,
	 * standing at the start Point of the Segment,
	 * looking towards the end Point of the Segment.
	 * 
	 * For example:
	 *         E
	 *        /
	 *     L / R
	 *      /
	 *     S
	 * 
	 * And:
	 *     S
	 *      \
	 *     R \ L
	 *        \
	 *         E
	 * 
	 * @param p
	 * @param s
	 * @return -1 if p is to the left of s,
	 * 0 if p and s are colinear,
	 * and 1 if  p is to the right of s.
	 */
	public int compare(Point p, Segment s) {
		return Utils.sign((s.pE.y - s.pS.y) * (p.x - s.pS.x) - (s.pE.x - s.pS.x) * (p.y - s.pS.y));
	}
	
	/**
	 * Calculates where the given segments intersect.
	 * 
	 * The intersection is returned as a value <code>u</code> for each Segment.
	 * <code>u</code> is 0 at the start Point of the Segment,
	 * <code>u</code> is 1 at the end Point of the Segment.
	 * 
	 * Each Segment represents an infinite line,
	 * intersections may occur before the start Point or after the end Point.
	 * <code>u</code> is less than 0 before the start Point,
	 * <code>u</code> is greater than 1 after the end Point.
	 * 
	 * Returns an array containing both value of <code>u</code>,
	 * index 0 contains the <code>u</code> of the first Segment,
	 * index 1 contains the <code>u</code> of the second Segment.
	 * Returns <code>null</code> if the given Segments are parallel,
	 * and therefore do not intersect.
	 * 
	 * For example:
	 * Given a Segment defined by start point S and end point E, with length D,
	 * the value of <code>u</code> is plotted at various points on the line:
	 * 
	 * -1       -0.5        0        0.5        1        1.5        2
	 *  |         |         |   0.25  |   0.75  |         |         |
	 *  |         |         |    |    |    |    |         |         |
	 *  --------------------S-------------------E--------------------
	 *  |_________D_________|_________D_________|_________D_________|
	 * 
	 * Returns <code>null</code> if the given Segments are parallel.
	 * 
	 * @param s1
	 * @param s2
	 * @return An array of two (2) double values,
	 * index 0 contains the <code>u</code> of the first Segment,
	 * index 1 contains the <code>u</code> of the second Segment,
	 * or <code>null</code> if the given Segments are parallel.
	 */
	public double[] collidesAt(Segment s1, Segment s2) {
		// Explanation of the math below:
		// Any point on s1 can be defined as "s1.pS + u1 * (s1.pE − s1.pS)".
		// Where u1 is a percentage of the distance between s1.pS and s1.pE.
		// This simplifies down to the two equations:
		//     y1 = s1.pS.y + u1 * (s1.pE.y - s1.pS.y)
		//     x1 = s1.pS.x + u1 * (s1.pE.x - s1.pS.x)
		// As we are looking for the point where both lines are equal:
		//     s1.pS.y + u1 * (s1.pE.y - s1.pS.y) == s2.pS.y + u2 * (s2.pE.y - s2.pS.y)
		//     s1.pS.x + u1 * (s1.pE.x - s1.pS.x) == s2.pS.x + u2 * (s2.pE.x - s2.pS.x)
		// We can solve for u1 and u2:
		//     u1 = ((s2.pE.x - s2.pS.x) * (s1.pS.y - s2.pS.y)) - ((s2.pE.y - s2.pS.y) * (s1.pS.x - s2.pS.x)) / ((s2.pE.y - s2.pS.y) * (s1.pE.x - s1.pS.x)) - ((s2.pE.x - s2.pS.x) * (s1.pE.y - s1.pS.y))
		//     u2 = ((s1.pE.x - s1.pS.x) * (s1.pS.y - s2.pS.y)) - ((s1.pE.y - s1.pS.y) * (s1.pS.x - s2.pS.x)) / ((s2.pE.y - s2.pS.y) * (s1.pE.x - s1.pS.x)) - ((s2.pE.x - s2.pS.x) * (s1.pE.y - s1.pS.y))
		// These both have the same denominator.
		// If the denominator is 0, u1 and u2 are undefined, lines are parallel.
		// Else intersection at u1 percent between s1.pS and s1.pE, similar for u2.
		
		double denom = ((s2.pE.y - s2.pS.y) * (s1.pE.x - s1.pS.x)) - ((s2.pE.x - s2.pS.x) * (s1.pE.y - s1.pS.y));
		
		if (denom == 0) {
			return null;
		}
		
		double numer1 = ((s2.pE.x - s2.pS.x) * (s1.pS.y - s2.pS.y)) - ((s2.pE.y - s2.pS.y) * (s1.pS.x - s2.pS.x));
		double numer2 = ((s1.pE.x - s1.pS.x) * (s1.pS.y - s2.pS.y)) - ((s1.pE.y - s1.pS.y) * (s1.pS.x - s2.pS.x));
		
		double u1 = numer1 / denom;
		double u2 = numer2 / denom;
		
		return new double[] {u1, u2};
	}
	
	/**
	 * Calculates a Point along the given Segment, based on the given double.
	 * Converts the <code>u</code> value,
	 * calculated in <code>collidesAt(Segment, Segment)</code>,
	 * into a usable Point.
	 * 
	 * <code>u</code> is defined in the documentation for
	 * <code>collidesAt(Segment, Segment)</code>.
	 * 
	 * @param segment
	 * @param u
	 * @return 
	 */
	public Point getPointAlongSegment(Segment segment, double u) {
		double y = segment.pS.y + u * (segment.pE.y - segment.pS.y);
		double x = segment.pS.x + u * (segment.pE.x - segment.pS.x);
		
		return new Point(y, x);
	}
	
	/**
	 * Calculates if the given Segment contains the given Point.
	 * A Segment "contains" a Point when the line drawn from
	 * the start Point to the end Point crosses the given Point.
	 * 
	 * @param s
	 * @param p
	 * @return true if Point p is on Segment s, false otherwise.
	 */
	public boolean contains(Segment s, Point p) {
		return Utils.equals((p.y - s.pS.y) / (s.pE.y - s.pS.y), (p.x - s.pS.x) / (s.pE.x - s.pS.x)) &&
				Utils.sign(s.pS.y - p.y) * Utils.sign(s.pS.y - s.pE.y) >= 0 && Utils.sign(s.pE.y - p.y) * Utils.sign(s.pE.y - s.pS.y) >= 0;
	}
	
	/**
	 * Calculates if the given Polygon contains the given Point.
	 * Returns an <code>int</code> value indicating the position of the Point,
	 * relative to the given Polygon.
	 * The value is -1 if the Point is external to the Polygon,
	 * 0 if the Point occurs on a Segment of the Polygon,
	 * 1 if the Point is internal and completely contained within the Polygon.
	 * 
	 * For example:
	 *      ____
	 *     /    \  .A
	 *    /  .C  \
	 *    \      /
	 *     \__._/
	 *         B
	 * 
	 * Given the above Polygon,
	 * Point A would return -1,
	 * Point B would return 0,
	 * Point C would return 1.
	 * 
	 * @param polygon
	 * @param point
	 * @return -1 if point is external to polygon,
	 * 0 if point is on one of segments in polygon,
	 * and 1 of point is internal to polygon.
	 */
	public int contains(Polygon polygon, Point point) {
		Collection<Segment> segments = new ArrayList<Segment>(polygon.segments.size());
		for (Segment segment : polygon.segments) {
			int result = compare(segment.pE, new Segment(point, segment.pS));
			if (result == 0) {
				// Colinear.
				if (Utils.equals(distance(segment.pS, point) + distance(point, segment.pE), distance(segment.pS, segment.pE))) {
					// On the segment.
					return 0;
				}
				// Point is colinear to, but not on, the segment,
				// ignore the segment,
				// point touches the line past the bounds of the segment.
			} else {
				// The end point should always be clockwise to the start point.
				if (result > 0) {
					segments.add(segment);
				} else {
					segments.add(new Segment(segment.pE, segment.pS));
				}
			}
		}
		
		// Cast a ray, straight up, starting from the point.
		// Count the number of times the ray intersects the polygon.
		Segment ray = new Segment(point, new Point(point.y + 1, point.x));
		int count = 0;
		for (Segment segment : segments) {
			double[] u = collidesAt(ray, segment);
			// Ignore end points to not count intersected corners twice.
			// Tangential corners will be counted either twice or zero times,
			// this does not affect the result.
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gte(u[1], 1)) {
				continue;
			}
			
			count++;
		}
		
		if (count % 2 == 0) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * Calculates if the given Segments overlap.
	 * To overlap, Segments must be completely collinear,
	 * and must each contain a common Point.
	 * 
	 * @param s1
	 * @param s2
	 * @return true if the given Segments overlap, false otherwise.
	 */
	public boolean overlaps(Segment s1, Segment s2) {
		return collidesAt(s1, s2) == null && (Abstract2DPlane.this.contains(s1, s2.pS) || Abstract2DPlane.this.contains(s1, s2.pS) || Abstract2DPlane.this.contains(s2, s1.pS) || Abstract2DPlane.this.contains(s2, s1.pE));
	}
	
	/**
	 * Returns the left normal of the given segment.
	 * The returned Segment is perpendicular to the given Segment,
	 * shares a start point with the given segment,
	 * and has an end point to the left of the given segment.
	 * 
	 * For example:
	 *          E -|
	 *          |  |
	 *    Pi/2__|  | Given segment
	 *       |  |  |
	 * E--------S -|
	 * |________|
	 *   Normal
	 * 
	 * @param s
	 * @return The normal to the left of the give Segment
	 */
	public Segment normalL(Segment s) {
		return new Segment(s.pS, new Point(s.pS.y + (s.pE.x - s.pS.x), s.pS.x + (s.pS.y - s.pE.y)));
	}
	
	/**
	 * Returns the right normal of the given segment.
	 * The returned segment is perpendicular to the given segment,
	 * shares a start point with the given segment,
	 * and has an end point to the right of the given segment.
	 * 
	 * For example:
	 *               E
	 *               |
	 * Given segment |__Pi/2
	 *               |  |
	 *               S--------E -|
	 *               |________|
	 *                 Normal
	 * 
	 * @param s
	 * @return The normal to the right of the given Segment.
	 */
	public Segment normalR(Segment s) {
		return new Segment(s.pS, new Point(s.pS.y + (s.pS.x - s.pE.x), s.pS.x +(s.pE.y - s.pS.y)));
	}
	
	/**
	 * Returns the FOV from the given Point, blocked by the given Segments.
	 * The field-of-view (FOV) is defined as a collection of Triangles,
	 * all with the given Point as their first vertex,
	 * and extending to collide with one of the given segments.
	 * 
	 * Any Point contained by the returned Triangles is within the FOV,
	 * it is possible to draw a line between it and the given Point,
	 * that does not intersect any of the given Segments.
	 * 
	 * Additionally, for each Triangle,
	 * the Segment with which it collided is returned.
	 * The second Segment of the Triangle will be contained by this Segment.
	 * 
	 * As an implementation tip:
	 * Assuming the given Point is fully enclosed in the given Segments,
	 * every Triangle will be adjacent to two other Triangles.
	 * A point that is on a vertex, is on the edge of the FOV.
	 * A point that is on exactly one Segment of the Triangles,
	 * is on the edge of the FOV.
	 * A point that is on exactly two Segments, and not on a vertex,
	 * is contained within the FOV.
	 * 
	 * It is better to call <code>getFOV(Point, Collection, double)</code>,
	 * to guarantee that the given Point is fully enclosed.
	 * 
	 * @param centre
	 * @param segments
	 * @return A Map of Triangle to Segments,
	 * the Triangles defining the field-of-view,
	 * along with the Segment with which the Triangle collided.
	 */
	public Map<Triangle, Segment> getFOV(Point centre, Collection<Segment> segments) {
		Map<Triangle, Segment> triangles = new LinkedHashMap<Triangle, Segment>();
		if (segments.isEmpty()) {
			return triangles;
		}
		
		// Normalise segments.
		// getFirstCollisions and getNextCollision require normalised segments.
		Collection<Segment> normSegs = normaliseSegments(segments, centre);
		
		ArrayList<Point> points = new ArrayList<Point>();
		Map<Point, Segment> startPoints = new HashMap<Point, Segment>();
		for (Segment segment : normSegs) {
			points.add(segment.pS);
			points.add(segment.pE);
			startPoints.put(segment.pS, segment);
		}
		
		// Sort points, so that they are parsed by the next step in order.
		points.sort((point1, point2) -> {
			// Sort clockwise, from y-axis.
			int result = Utils.sign(angle(point1.y - centre.y, point1.x - centre.x) - angle(point2.y - centre.y, point2.x - centre.x));
			
			// If points are colinear.
			if (result == 0) {
				// Sort by distance.
				result = Utils.sign(distance(centre, point1) - distance(centre, point2));
			}
			
			// If points colinear, equidistant, and on opposite sides of centre.
			if (result == 0 && !Utils.equals(point1, point2)) {
				// Arbitrary selection,
				// but to make sure they don't appear as equal.
				result = -1;
			}
			
			return result;
		});
		
		Iterator<Point> iterator = points.iterator();
		Point next = iterator.next();
		Collision[] firstCollisions = getNextCollisions(new Segment(centre, next), normSegs);
		// Point of first collision, on last clockwise segment.
		Point initPoint = firstCollisions[0].point;
		// Point on current segment, where current triangle of view begins.
		Point lastPoint = firstCollisions[1].point;
		Segment currentSegment = firstCollisions[1].segment;
		while (iterator.hasNext()) {
			next = iterator.next();
			if (next.equals(currentSegment.pE)) {
				// End of current segment.
				// Add triangle from last to the end of current segment.
				triangles.put(new Triangle(centre, lastPoint, next), currentSegment);
				
				Collision collision = getNextCollision(new Segment(centre, next), normSegs);
				lastPoint = collision.point;
				currentSegment = collision.segment;
			} else if (startPoints.containsKey(next)) {
				// TODO: If statement above, otherwise endpoints are tested and sometimes closer than the current segment.
				
				// Cast a ray from centre to the next start point,
				// and find the point on the current segment.
				double[] u = collidesAt(new Segment(centre, next), currentSegment);
				Point pointOnCurrentSegment = getPointAlongSegment(currentSegment, u[1]);
				
				// If next point is closer to centre than current segment.
				// Or if the point is touching the current segment,
				// and the new segment is in front of the current segment.
				double distanceD = distance(centre, next) - distance(centre, pointOnCurrentSegment);
				if (Utils.lt(distanceD, 0) || (Utils.equals(distanceD, 0) && Utils.gt(Abstract2DPlane.this.compare(startPoints.get(next).pE, currentSegment), 0))) {
					triangles.put(new Triangle(centre, lastPoint, pointOnCurrentSegment), currentSegment);
					
					lastPoint = next;
					currentSegment = startPoints.get(next);
				}
			}
		}
		triangles.put(new Triangle(centre, lastPoint, initPoint), currentSegment);
		
		return triangles;
	}
	
	/**
	 * Completely encloses the given Point, before calculating FOV.
	 * Encloses the given Point with Segments, at <code>limit</code> distance,
	 * one segment along the x-axis at <code>centre.y - limit</code>,
	 * one segment along the x-axis at <code>centre.y + limit</code>,
	 * one segment along the y-axis at <code>centre.x - limit</code>,
	 * one segment along the y-axis at <code>centre.x + limit</code>.
	 * 
	 * This functions identically to <code>getFOV(Point, Collection)</code>.
	 * 
	 * @param centre
	 * @param segments
	 * @param limit
	 * @return 
	 */
	public Map<Triangle, Segment> getFOV(Point centre, Collection<Segment> segments, double limit) {
		Collection<Segment> modSegments = new ArrayList<Segment>(segments.size() + 4);
		
		modSegments.addAll(segments);
		modSegments.add(new Segment(new Point(centre.y - limit, centre.x - limit), new Point(centre.y + limit, centre.x - limit)));
		modSegments.add(new Segment(new Point(centre.y + limit, centre.x - limit), new Point(centre.y + limit, centre.x + limit)));
		modSegments.add(new Segment(new Point(centre.y + limit, centre.x + limit), new Point(centre.y - limit, centre.x + limit)));
		modSegments.add(new Segment(new Point(centre.y - limit, centre.x + limit), new Point(centre.y - limit, centre.x - limit)));
		
		return getFOV(centre, modSegments);
	}
	
	/**
	 * Sorts Segments clockwise based on the Segment start point,
	 * ensures end Points are always clockwise from start Points,
	 * and removes collinear Segments.
	 * 
	 * @param segments
	 * @param relativeTo
	 * @return The given Segments, sorted clockwise,
	 * with end Points always clockwise from start Points,
	 * with collinear Segments removed.
	 */
	private Collection<Segment> normaliseSegments(Collection<Segment> segments, Point relativeTo) {
		TreeSet<Segment> sorted = new TreeSet<Segment>((segment1, segment2) -> {
			// Compare clockwise-ness of starting points.
			// int result = contains(new Segment(relativeTo, segment2.pS), segment1.pS);
			int result = Utils.sign(angle(segment1.pS, relativeTo) - angle(segment2.pS, relativeTo));
			
			// If all three points are colinear, get closest starting point.
			if (result == 0) {
				result = Utils.sign(distance(relativeTo, segment1.pS) - distance(relativeTo, segment2.pS));
			}
			
			// If both segments start at the same point, sort clockwise.
			if (result == 0) {
				result = Abstract2DPlane.this.compare(segment1.pE, segment2);
			}
			return result;
		});
		
		for (Segment segment : segments) {
			// If relativeTo is colinear to this segment, ignore this segment.
			// As lines are technically 1 dimensional,
			// nothing cast from relativeTo can interact with it anyway.
			// Also prevents problems when relativeTo intersects the segment.
			if (Abstract2DPlane.this.compare(relativeTo, segment) == 0) {
				continue;
			}
			
			// From the relativeTo perspective,
			// the end point should always be clockwise from the start point,
			// if all points are colinear (only included here for safety),
			// the start point should be closer than the end point.
			int result = Abstract2DPlane.this.compare(segment.pE, new Segment(relativeTo, segment.pS));
			if ((result < 0) || (result == 0 && distance(relativeTo, segment.pS) > distance(relativeTo, segment.pE))) {
				segment = new Segment(segment.pE, segment.pS);
			}
			
			sorted.add(segment);
		}
		
		return sorted;
	}
	
	/**
	 * Returns the closest Collisions along the given ray.
	 * Returns an array of two Collision elements,
	 * collision[0] is on a Segment that extends left of the Collision Point,
	 * collision[1] is on a Segment that extends right of the Collision Point.
	 * Assumes that the Collection of segments has been normalised.
	 * 
	 * @param ray
	 * @param normSegs
	 * @return The closest Collisions along the given ray,
	 * index 0 extends left of the Collision Point,
	 * index 1 extends right of the Collision Point.
	 */
	private Collision[] getNextCollisions(Segment ray, Collection<Segment> normSegs) {
		List<Collision> antiClockwise = new ArrayList<Collision>();
		List<Collision> clockwise = new ArrayList<Collision>();
		
		for (Segment segment : normSegs) {
			double[] u = collidesAt(ray, segment);
			
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gt(u[1], 1)) {
				// No collision.
				continue;
			}
			
			Point point = getPointAlongSegment(segment, u[1]);
			if (Utils.gt(u[1], 0)) {
				antiClockwise.add(new Collision(point, segment));
			}
			if (Utils.lt(u[1], 1)) {
				clockwise.add(new Collision(point, segment));
			}
		}
		
		Collections.sort(antiClockwise, (c1, c2) -> {
			// Compare the distance between the collision points.
			int result = Utils.sign(distance(ray.pS, c1.point) - distance(ray.pS, c2.point));
			
			// If collision points are equidistant (should be the same point),
			// the most anti-clockwise element should be first,
			// in effect, sorted clockwise.
			if (result == 0) {
				result = Abstract2DPlane.this.compare(c1.segment.pS, c2.segment);
			}
			return result;
		});
		Collections.sort(clockwise, (c1, c2) -> {
			// Compare the distance between the collision points.
			int result = Utils.sign(distance(ray.pS, c1.point) - distance(ray.pS, c2.point));
			
			// If collision points are equidistant (should be the same point),
			// the most clockwise element should be first,
			// in effect, sorted anti-clockwise.
			if (result == 0) {
				result = -Abstract2DPlane.this.compare(c1.segment.pE, c2.segment);
			}
			return result;
		});
		
		return new Collision[]{antiClockwise.get(0), clockwise.get(0)};
	}
	
	/**
	 * Returns the closest Collision along the given ray.
	 * A slightly faster implementation of getNextCollisions,
	 * which only returns the next clockwise Collision.
	 * Intended to be used when processing points in a clockwise manner.
	 * Assumes that the Collection of Segments has been normalised.
	 * 
	 * @param ray
	 * @param normSegs
	 * @return The closest clockwise Collision along the given ray.
	 */
	private Collision getNextCollision(Segment ray, Collection<Segment> normSegs) {		
		List<Collision> collisions = new LinkedList<Collision>();
		
		for (Segment segment : normSegs) {
			double[] u = collidesAt(ray, segment);
			
			// Don't collide with end of segments.
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gte(u[1], 1)) {
				// No collision.
				continue;
			}
			
			Point point = getPointAlongSegment(segment, u[1]);
			collisions.add(new Collision(point, segment));
		}
		
		// Get the closest collision.
		// If two segments start at collision point, select the last segment,
		// based on normalisation, this is the most clockwise segment.
		Collision closestCollision = null;
		double closestCollisionDistance = Double.POSITIVE_INFINITY;
		for (Collision collision : collisions) {
			double collDistance = distance(ray.pS, collision.point);
			if (Utils.lte(collDistance, closestCollisionDistance)) {
				closestCollision = collision;
				closestCollisionDistance = collDistance;
			}
		}
		
		return closestCollision;
	}
	
	/**
	 * Represents a collision between a ray and a line segment.
	 */
	public class Collision {
		public final Point point;
		public final Segment segment;

		public Collision(Point point, Segment segment) {
			this.point = point;
			this.segment = segment;
		}
	}
	
	/**
	 * Determines if the given Polygons are colliding.
	 * Returns null if the given Polygons are not colliding,
	 * otherwise, returns a Segment representing the smallest possible vector,
	 * that if applied to the second argument,
	 * will cause the Polygons to no longer collide.
	 * 
	 * This is an implementation of the Separating Axis Theorem.
	 * 
	 * @param p1
	 * @param p2
	 * @return The a Segment representing smallest possible vector,
	 * that if applied to the second argument,
	 * will cause the Polygons to no longer collide,
	 * or null if the Polygons are not colliding.
	 * @throws IllegalArgumentException If either given Polygon is not convex.
	 */
	public Segment collides(Polygon p1, Polygon p2) {
		if (!isConvex(p1)) {
			throw new IllegalArgumentException("Polygon " + p1 + " is not convex.");
		}
		if (!isConvex(p2)) {
			throw new IllegalArgumentException("Polygon " + p2 + " is not convex.");
		}
		
		List<Segment> segments = new ArrayList<Segment>(p1.segments.size() + p2.segments.size());
		segments.addAll(p1.segments);
		segments.addAll(p2.segments);
		
		double dist = Double.NEGATIVE_INFINITY;
		Segment distNorm = null;
		int distMult = 0;
		for (Segment segm : segments) {
			double yD = segm.pE.y - segm.pS.y;
			double xD = segm.pE.x - segm.pS.x;
			Segment norm = new Segment(segm.pS, new Point(segm.pS.y - xD, segm.pS.x + yD));
			
			double min1 = Double.POSITIVE_INFINITY;
			double max1 = Double.NEGATIVE_INFINITY;
			for (Point point : p1.points) {
				Segment test = new Segment(point, new Point(point.y + yD, point.x + xD));
				double u = collidesAt(test, norm)[1];
				if (u < min1) {
					min1 = u;
				}
				if (u > max1) {
					max1 = u;
				}
			}
			
			double min2 = Double.POSITIVE_INFINITY;
			double max2 = Double.NEGATIVE_INFINITY;
			for (Point point : p2.points) {
				Segment test = new Segment(point, new Point(point.y + yD, point.x + xD));
				double u = collidesAt(test, norm)[1];
				if (u < min2) {
					min2 = u;
				}
				if (u > max2) {
					max2 = u;
				}
			}
						
			double dist1 = min1 - max2;
			double dist2 = min2 - max1;
			
			if (dist1 > 0 || dist2 > 0) {
				return null;
			}
			
			double normalLength = distance(norm.pS, norm.pE);
			dist1 = dist1 * normalLength;
			dist2 = dist2 * normalLength;
			
			if (dist1 <= 0 && dist1 > dist) {
				dist = dist1;
				distNorm = norm;
				distMult = 1;
			}
			if (dist2 <= 0 && dist2 > dist) {
				dist = dist2;
				distNorm = norm;
				distMult = -1;
			}
		}
		
		return new Segment(new Point(0, 0), getDeltaInDirection(distNorm.pE.y - distNorm.pS.y, distNorm.pE.x - distNorm.pS.x, dist * distMult));
	}
	
	/**
	 * Determines if a given Polygon is convex.
	 * A polygon is convex if all interior angles are less than Pi radians.
	 * 
	 * @param polygon
	 * @return true if the given Polygon is convex, false otherwise.
	 */
	private boolean isConvex(Polygon polygon) {
		boolean clockwise = true;
		
		List<Point> points = new ArrayList<Point>(polygon.points.size() + 2);
		points.addAll(polygon.points);
		points.add(polygon.points.get(0));
		points.add(polygon.points.get(1));
		Iterator<Point> iterator = points.iterator();
		Point p1 = iterator.next();
		Point p2 = iterator.next();
		while (iterator.hasNext()) {
			Point p3 = iterator.next();
			
			boolean res = Utils.gte(compare(p3, new Segment(p1, p2)), 0);
			if (!clockwise && res) {
				return false;
			}
			
			clockwise = res;
			
			p1 = p2;
			p2 = p3;
		}
		
		return true;
	}
}
