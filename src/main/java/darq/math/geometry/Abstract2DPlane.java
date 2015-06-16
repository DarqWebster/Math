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
	public abstract double distance(double yD, double xD);
	
	public double distance(Point p1, Point p2) {
		return distance(p2.y - p1.y, p2.x - p1.x);
	}
	
	public double angle(double yD, double xD) {
		double result = Math.atan2(xD, yD);
		if (result < 0) {
			result = 2 * Math.PI + result;
		}
		return result;
	}
	
	public double angle(Point from, Point to) {
		return angle(to.y - from.y, to.x - from.x);
	}
	
	public Point adjust(Point point, double yD, double xD) {
		return new Point(point.y + yD, point.x + xD);
	}
	
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
	
	public Point getDeltaInDirection(double yD, double xD, double step) {
		double reference = distance(yD, xD);
		double ratio = step / reference;
		return new Point(yD * ratio, xD * ratio);
	}
	
	/**
	 *
	 * @param p
	 * @param s
	 * @return -1 if p is to the left of s, 0 if p and s are colinear, and 1 if  p is to the right of s.
	 */
	public int compare(Point p, Segment s) {
		return Utils.sign((s.pE.y - s.pS.y) * (p.x - s.pS.x) - (s.pE.x - s.pS.x) * (p.y - s.pS.y));
	}
	
	public double[] collidesAt(Segment s1, Segment s2) {
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
	
	public Point getPointAlongSegment(Segment segment, double u) {
		double y = segment.pS.y + u * (segment.pE.y - segment.pS.y);
		double x = segment.pS.x + u * (segment.pE.x - segment.pS.x);
		
		return new Point(y, x);
	}
	
	/**
	 * 
	 * @param s
	 * @param p
	 * @return true if p is on s, false otherwise.
	 */
	public boolean contains(Segment s, Point p) {
//		double yD = segment.pE.y - segment.pS.y;
//		double xD = segment.pE.x - segment.pS.x;
//		
//		Segment perpendicular = new Segment(point, new Point(point.y - xD, point.x + yD));
//		double[] u = collidesAt(segment, perpendicular);
//		
//		return Utils.gte(u[0], 0) && Utils.lte(u[0], 1) && Utils.equals(u[1], 0);
		
//		return collidesAt(new Segment(segment.pS, point), new Segment(point, segment.pE)) == null &&
//				Utils.equals(distance(segment.pS, point) + distance(point, segment.pE), distance(segment.pS, segment.pE));
		
		return Utils.equals((p.y - s.pS.y) / (s.pE.y - s.pS.y), (p.x - s.pS.x) / (s.pE.x - s.pS.x)) &&
				Utils.sign(s.pS.y - p.y) * Utils.sign(s.pS.y - s.pE.y) >= 0 && Utils.sign(s.pE.y - p.y) * Utils.sign(s.pE.y - s.pS.y) >= 0;
	}
	
	/**
	 * 
	 * @param polygon
	 * @param point
	 * @return -1 if point is external to polygon, 0 if point is on one of segments in polygon, and 1 of point is internal to polygon.
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
	
	public boolean overlaps(Segment s1, Segment s2) {
		return collidesAt(s1, s2) == null && (Abstract2DPlane.this.contains(s1, s2.pS) || Abstract2DPlane.this.contains(s1, s2.pS) || Abstract2DPlane.this.contains(s2, s1.pS) || Abstract2DPlane.this.contains(s2, s1.pE));
	}
	
	/**
	 * Returns the left normal of the given segment.
	 * The returned segment is perpendicular to the given segment,
	 * shares a start point with the given segment,
	 * and has an end point to the left of the given segment.
	 * @param s
	 * @return 
	 */
	public Segment normalL(Segment s) {
		return new Segment(s.pS, new Point(s.pS.y + (s.pE.x - s.pS.x), s.pS.x + (s.pS.y - s.pE.y)));
	}
	
		/**
	 * Returns the right normal of the given segment.
	 * The returned segment is perpendicular to the given segment,
	 * shares a start point with the given segment,
	 * and has an end point to the right of the given segment.
	 * @param s
	 * @return 
	 */
	public Segment normalR(Segment s) {
		return new Segment(s.pS, new Point(s.pS.y + (s.pS.x - s.pE.x), s.pS.x +(s.pE.y - s.pS.y)));
	}
	
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
	 * Sorts segments clockwise based on start point,
	 * ensures end points are always clockwise to start points,
	 * and removes colinear segments.
	 * @param segments
	 * @param relativeTo
	 * @return 
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
	 * Returns the closest collisions along the given ray.
	 * Returns an array of two collision elements,
	 * collision[0] is on a segment that extends left of the collision point,
	 * collision[1] is on a segment that extends right of the collision point.
	 * Assumes that the Collection of segments has been normalised.
	 * @param ray
	 * @param normSegs
	 * @return 
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
	 * Returns the closest collision along the given ray.
	 * A slightly faster implementation of getNextCollisions,
	 * which only returns the next clockwise collision.
	 * Intended to be used when processing points in a clockwise manner.
	 * Assumes that the Collection of segments has been normalised.
	 * @param ray
	 * @param normSegs
	 * @return 
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
	
	public class Collision {
		public final Point point;
		public final Segment segment;

		public Collision(Point point, Segment segment) {
			this.point = point;
			this.segment = segment;
		}
	}
	
	/**
	 * Determines if the given polygons are colliding.
	 * Returns null if the given polygons are not colliding,
	 * otherwise, returns the smallest possible vector,
	 * that if applied to the second argument,
	 * will cause the polygons to no longer collide.
	 * @param p1
	 * @param p2
	 * @return 
	 */
	public Segment collides(Polygon p1, Polygon p2) {
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
}
