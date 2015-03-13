package darq.math.geometry;

import darq.math.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Craig.Webster
 */
public abstract class Abstract2DPlane {
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
	 * @return -1 if p is to the left of s, 0 if p and s are colinear, and 1 if  p is to the right of 1.
	 */
	public int compare(Segment s, Point p) {
		return Utils.sign((s.pE.y - s.pS.y) * (p.x - s.pS.x) - (s.pE.x - s.pS.x) * (p.y - s.pS.y));
	}
	
	public Point adjust(Point point, double yD, double xD) {
		return new Point(point.y + yD, point.x + xD);
	}
	
	public Segment adjust(Segment segment, double yD, double xD) {
		return new Segment(adjust(segment.pS, yD, xD), adjust(segment.pE, yD, xD));
	}
	
	public List<Collision> castRay(Segment ray, Collection<Segment> segments) {		
		HashMap<Point, Collection<Segment>> touches = new HashMap<Point, Collection<Segment>>();
		List<Collision> collisions = new ArrayList<Collision>();
		
		for (Segment segment : segments) {
			double[] u = collidesAt(ray, segment);
			
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gt(u[1], 1)) {
				// No collision.
				continue;
			}
			
			Point point = getPointAlongSegment(segment, u[1]);
			
			if (Utils.equals(u[1], 0) || Utils.equals(u[1], 1)) {
				// Just touching.
				Collection<Segment> touchSegments = touches.get(point);
				if (touchSegments == null) {
					touchSegments = new ArrayList<Segment>();
					touches.put(point, touchSegments);
				}
				touchSegments.add(segment);
			} else {
				// Colliding.
				collisions.add(new Collision(point, segment));
			}
		}
		
		for (Map.Entry<Point, Collection<Segment>> entry : touches.entrySet()) {
			if (entry.getValue().size() == 1) {
				continue;
			}
			
			Collection<Segment> clockwise = new LinkedList<Segment>();
			boolean anticlockwise = false;
			for (Segment touchedSegment : entry.getValue()) {
				int dir = compare(ray, touchedSegment.pS) + compare(ray, touchedSegment.pE);
				if (dir > 0) {
					clockwise.add(touchedSegment);
				}
				if (dir < 0) {
					anticlockwise = true;
				}
			}
			
			if (!clockwise.isEmpty() && anticlockwise) {
				Iterator<Segment> iterator = clockwise.iterator();
				Segment closestClockwise = iterator.next();
				while (iterator.hasNext()) {
					Segment test = iterator.next();
					Point other = other(test, entry.getKey());
					if (compare(closestClockwise, other) > 0) {
						closestClockwise = test;
					}
				}
				
				collisions.add(new Collision(entry.getKey(), closestClockwise));
			}
		}
		
		collisions.sort((collision1, collision2) -> {
			return Utils.compare(distance(ray.pS, collision1.point), distance(ray.pS, collision2.point));
		});
		
		return collisions;
	}
	
	private Collision getNextCollision(Segment ray, Collection<Segment> segments) {		
		HashMap<Point, Segment> collisions = new HashMap<Point, Segment>();
		
		for (Segment segment : segments) {
			double[] u = collidesAt(ray, segment);
			
			// Don't collide with end of segments.
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gte(u[1], 1)) {
				// No collision.
				continue;
			}
			
			Point point = getPointAlongSegment(segment, u[1]);
			collisions.put(point, segment);
		}
		
		if (collisions.isEmpty()) {
			return null;
		}
		
		Iterator<Point> iterator = collisions.keySet().iterator();
		Point closestCollision = iterator.next();
		double closestCollisionDistance = distance(ray.pS, closestCollision);
		while (iterator.hasNext()) {
			Point collision = iterator.next();
			double collisionDistance = distance(ray.pS, collision);
			if (Utils.lt(collisionDistance, closestCollisionDistance)) {
				closestCollision = collision;
				closestCollisionDistance = collisionDistance;
			}
		}
		
		return new Collision(closestCollision, collisions.get(closestCollision));
		
	}
	
	/**
	 * 
	 * Assumes that the Collection of segments has been normalised.
	 * @param ray
	 * @param normSegs
	 * @return 
	 */
	private Collision[] getFirstCollisions(Segment ray, Collection<Segment> normSegs) {
		TreeSet<Collision> antiClockwise = new TreeSet<Collision>((c1, c2) -> {
			// Compare the distance between the collision points.
			int result = Utils.sign(distance(ray.pS, c1.point) - distance(ray.pS, c2.point));
			
			// If collision points are equidistant (should be the same point),
			// the most anti-clockwise element should be first,
			// in effect, sorted clockwise.
			if (result == 0) {
				result = compare(c2.segment, c1.segment.pS);
			}
			return result;
		});
		TreeSet<Collision> clockwise = new TreeSet<Collision>((c1, c2) -> {
			// Compare the distance between the collision points.
			int result = Utils.sign(distance(ray.pS, c1.point) - distance(ray.pS, c2.point));
			
			// If collision points are equidistant (should be the same point),
			// the most anti-clockwise element should be first,
			// in effect, sorted anti-clockwise.
			if (result == 0) {
				result = -compare(c2.segment, c1.segment.pE);
			}
			return result;
		});
		
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
		
		return new Collision[]{antiClockwise.first(), clockwise.first()};
	}
	
	/**
	 * Sorts segments based on start point,
	 * ensures end points are always clockwise to start points,
	 * and removes colinear segments.
	 * 
	 * @param segments
	 * @param relativeTo
	 * @return 
	 */
	private Collection<Segment> normaliseSegments(Collection<Segment> segments, Point relativeTo) {
		TreeSet<Segment> sorted = new TreeSet<Segment>((segment1, segment2) -> {
			// Compare clockwise-ness of starting points.
			// int result = compare(new Segment(relativeTo, segment2.pS), segment1.pS);
			int result = Utils.sign(angle(segment1.pS, relativeTo) - angle(segment2.pS, relativeTo));
			
			// If all three points are colinear, get closest starting point.
			if (result == 0) {
				result = Utils.sign(distance(relativeTo, segment1.pS) - distance(relativeTo, segment2.pS));
			}
			
			// If both segments start at the same point, sort clockwise.
			if (result == 0) {
				result = compare(segment2, segment1.pE);
			}
			return result;
		});
		
		for (Segment segment : segments) {
			// If the relativeTo is colinear to this segment, ignore this segment.
			// As lines are technically 1 dimensional,
			// nothing cast from relativeTo can interact with it anyway.
			// Also prevents problems when relativeTo intersects the segment.
			if (compare(segment, relativeTo) == 0) {
				continue;
			}
			
			// From the relativeTo perspective,
			// the end point should always be clockwise from the start point,
			// if all points are colinear (only included here for safety),
			// the start point should be closer than the end point.
			int result = compare(new Segment(relativeTo, segment.pS), segment.pE);
			if ((result < 0) || (result == 0 && distance(relativeTo, segment.pS) > distance(relativeTo, segment.pE))) {
				segment = new Segment(segment.pE, segment.pS);
			}
			
			sorted.add(segment);
		}
		
		return sorted;
	}
	
	private Point other(Segment segment, Point point) {
		return (point.equals(segment.pS)) ? segment.pE : segment.pS;
	}
	
	public Collection<Triangle> getFOV(Point centre, Collection<Segment> segments) {
		return getFOVLotsOfIfs(centre, segments).keySet();
	}
	
	public Collection<Triangle> getFOV(Point centre, Collection<Segment> segments, double limit) {
		return getFOVWithBlockingSegments(centre, segments, limit).keySet();
	}
	
	public Map<Triangle, Segment> getFOVWithBlockingSegments(Point centre, Collection<Segment> segments, double limit) {
		Collection<Segment> modSegments = new ArrayList<Segment>(segments.size() + 4);
		
		modSegments.addAll(segments);
		modSegments.add(new Segment(new Point(centre.y - limit, centre.x - limit), new Point(centre.y + limit, centre.x - limit)));
		modSegments.add(new Segment(new Point(centre.y + limit, centre.x - limit), new Point(centre.y + limit, centre.x + limit)));
		modSegments.add(new Segment(new Point(centre.y + limit, centre.x + limit), new Point(centre.y - limit, centre.x + limit)));
		modSegments.add(new Segment(new Point(centre.y - limit, centre.x + limit), new Point(centre.y - limit, centre.x - limit)));
		
		return getFOVLotsOfIfs(centre, modSegments);
	}
	
	private Map<Triangle, Segment> getFOVLotsOfIfs(Point centre, Collection<Segment> segments) {
		Map<Triangle, Segment> triangles = new LinkedHashMap<Triangle, Segment>();
		if (segments.isEmpty()) {
			return triangles;
		}
		
		Collection<Segment> normSegs = normaliseSegments(segments, centre);
		
		ArrayList<Point> points = new ArrayList<Point>();
		Map<Point, Segment> startPointToSegment = new HashMap<Point, Segment>();
		for (Segment segment : normSegs) {
			points.add(segment.pS);
			points.add(segment.pE);
			startPointToSegment.put(segment.pS, segment);
		}
		points.sort((point1, point2) -> {
//			int result = Abstract2DPlane.compare(new LineSegment(centre, point2), point1);
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
		
		/*
			col = castRay(new LineSegment(centre, next), normSegs);
			
			------next------	init	last	currentSegment
			start +	direct	:	col.p	next	startPointToSegment.get(next)
			end	  +	direct	:	next	col.p	col.s
			start +	indirect:	col.p	col.p	col.s
			end	  +	indirect:	col.p	col.p	col.s
		*/
		Iterator<Point> iterator = points.iterator();
		Point next = iterator.next();
		Collision[] firstCollisions = getFirstCollisions(new Segment(centre, next), normSegs);
		Point initPoint = firstCollisions[0].point;
		Point lastPoint = firstCollisions[1].point;
		Segment currentSegment = firstCollisions[1].segment;
		while (iterator.hasNext()) {
			next = iterator.next();
			if (next.equals(currentSegment.pE)) {
				triangles.put(new Triangle(centre, lastPoint, next), currentSegment);
				
				Collision collision = getNextCollision(new Segment(centre, next), normSegs);
				lastPoint = collision.point;
				currentSegment = collision.segment;
			} else if (startPointToSegment.containsKey(next)) {
				// TODO: If statement above, otherwise endpoints are tested and sometimes closer than the current segment.
				double[] u = collidesAt(new Segment(centre, next), currentSegment);
				Point pointOnCurrentSegment = getPointAlongSegment(currentSegment, u[1]);
				
				// If next point is closer to centre than current segment.
				// Or if the point is touching the current segment,
				// and the new segment is in front of the current segment.
				double distanceD = distance(centre, next) - distance(centre, pointOnCurrentSegment);
				if (Utils.lt(distanceD, 0) || (Utils.equals(distanceD, 0) && Utils.gt(compare(currentSegment, startPointToSegment.get(next).pE), 0))) {
					triangles.put(new Triangle(centre, lastPoint, pointOnCurrentSegment), currentSegment);
					
					lastPoint = next;
					currentSegment = startPointToSegment.get(next);
				}
			}
		}
		triangles.put(new Triangle(centre, lastPoint, initPoint), currentSegment);
		
		return triangles;
	}
	
	public abstract double distance(double yD, double xD);
	
	public double distance(Point p1, Point p2) {
		return distance(p2.y - p1.y, p2.x - p1.x);
	}
	
	public Point getDeltaInDirection(double yD, double xD, double step) {
		double reference = distance(yD, xD);
		double ratio = step / reference;
		return new Point(yD * ratio, xD * ratio);
	}
	
	public double angle(Point from, Point to) {
		return angle(to.y - from.y, to.x - from.x);
	}
	
	public double angle(double yD, double xD) {
		double result = Math.atan2(xD, yD);
		if (result < 0) {
			result = 2 * Math.PI + result;
		}
		return result;
	}
	
	public int contains(Polygon polygon, Point point) {
		Collection<Segment> segments = new ArrayList<Segment>(polygon.segments.size());
		for (Segment segment : polygon.segments) {
			int result = compare(new Segment(point, segment.pS), segment.pE);
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
	
	public boolean contains(Segment s, Point p) {
//		double yD = segment.pE.y - segment.pS.y;
//		double xD = segment.pE.x - segment.pS.x;
//		
//		Segment perpendicular = new Segment(point, new Point(point.y - xD, point.x + yD));
//		double[] u = collidesAt(segment, perpendicular);
//		
//		return Utils.gte(u[0], 0) && Utils.lte(u[0], 1) && Utils.equals(u[1], 0);
		
		return Utils.equals((p.y - s.pS.y) / (s.pE.y - s.pS.y), (p.x - s.pS.x) / (s.pE.x - s.pS.x)) &&
				Utils.sign(s.pS.y - p.y) * Utils.sign(s.pS.y - s.pE.y) >= 0 && Utils.sign(s.pE.y - p.y) * Utils.sign(s.pE.y - s.pS.y) >= 0;
		
//		return collidesAt(new Segment(segment.pS, point), new Segment(point, segment.pE)) == null &&
//				Utils.equals(distance(segment.pS, point) + distance(point, segment.pE), distance(segment.pS, segment.pE));
	}
	
	public boolean overlaps(Segment s1, Segment s2) {
		return collidesAt(s1, s2) == null && (contains(s1, s2.pS) || contains(s1, s2.pS) || contains(s2, s1.pS) || contains(s2, s1.pE));
	}
	
	public class Collision {
		public final Point point;
		public final Segment segment;

		public Collision(Point point, Segment segment) {
			this.point = point;
			this.segment = segment;
		}
	}
}
