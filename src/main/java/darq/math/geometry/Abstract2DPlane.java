package darq.math.geometry;

import darq.math.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Craig.Webster
 */
public abstract class Abstract2DPlane {
	private double[] collidesAt(Segment s1, Segment s2) {
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
	
	public Point collides(Segment s1, LineType t1, Segment s2, LineType t2) {
		double[] u = collidesAt(s1, s2);
		if (u == null) {
			return null;
		}
		
		if (!(t1.isOn(u[0]) && t2.isOn(u[1]))) {
			return null;
		}
		
		return getPointAlongSegment(s1, u[0]);
	}
	
	public Point closestPointOnSegment(Segment segment, LineType lineType, Point point) {
		// Closest point is perpendicular to the line itself.
		double dy = segment.pE.y - segment.pS.y;
		double dx = segment.pE.x - segment.pS.x;
		
		Segment perpendicular = new Segment(point, new Point(point.y - dx, point.x + dy));
		
		double u = collidesAt(segment, perpendicular)[0];
		
		// Lock to line type;
		if (Utils.lte(u, lineType.min())) {
			u = lineType.min();
		}
		if (Utils.gte(u, lineType.max())) {
			u = lineType.max();
		}
		
		return new Point(segment.pS.y + u * (segment.pE.y - segment.pS.y), segment.pS.x + u * (segment.pE.x - segment.pS.x));
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
	
	public Map<Point, Collection<Segment>> getSegmentsByPoint(Collection<Segment> segments) {
		Map<Point, Collection<Segment>> segmentsByPoint = new HashMap<Point, Collection<Segment>>();
		
		for (Segment segment : segments) {
			Collection<Segment> currentSegments;
			
			currentSegments = segmentsByPoint.get(segment.pS);
			if (currentSegments == null) {
				currentSegments = new ArrayList<Segment>(2);
				segmentsByPoint.put(segment.pS, currentSegments);
			}
			currentSegments.add(segment);
			
			currentSegments = segmentsByPoint.get(segment.pE);
			if (currentSegments == null) {
				currentSegments = new ArrayList<Segment>(2);
				segmentsByPoint.put(segment.pE, currentSegments);
			}
			currentSegments.add(segment);
		}
		
		return segmentsByPoint;
	}
	
	public RayCollision castRay(Segment ray, Collection<Segment> segments) {		
		HashMap<Point, Collection<Segment>> touches = new HashMap<Point, Collection<Segment>>();
		HashMap<Point, Segment> collisions = new HashMap<Point, Segment>();
		
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
				collisions.put(point, segment);
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
				
				collisions.put(entry.getKey(), closestClockwise);
			}
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
		
		return new RayCollision(closestCollision, collisions.get(closestCollision));
		
	}
	
	private RayCollision getNextCollision(Segment ray, Collection<Segment> segments) {		
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
		
		return new RayCollision(closestCollision, collisions.get(closestCollision));
		
	}
	
	private Segment[] getFirstCollisions(Segment ray, Collection<Segment> segments) {
		System.out.println("firstCollisions. ray: " + ray + ".");
		TreeSet<Segment> antiClockwise = new TreeSet<Segment>((s1, s2) -> {
			int result = Utils.sign(distance(ray.pS, s1.pE) - distance(ray.pS, s2.pE));
			if (result == 0) {
				result = compare(s2, s1.pS);
			}
			return result;
		});
		TreeSet<Segment> clockwise = new TreeSet<Segment>((s1, s2) -> {
			int result = Utils.sign(distance(ray.pS, s1.pS) - distance(ray.pS, s2.pS));
			if (result == 0) {
				result = compare(s2, s1.pE);
			}
			return result;
		});
		
		for (Segment segment : segments) {
			double[] u = collidesAt(ray, segment);
			
			if (u == null || Utils.lt(u[0], 0) || Utils.lt(u[1], 0) || Utils.gt(u[1], 1)) {
				// No collision.
				continue;
			}
			
			Point point = getPointAlongSegment(segment, u[1]);
			if (Utils.gt(u[1], 0)) {
				antiClockwise.add(new Segment(segment.pS, point));
			}
			if (Utils.lt(u[1], 1)) {
				clockwise.add(new Segment(point, segment.pE));
			}
		}
		
		System.out.println("antiClockwise:");
		for (Segment segment : antiClockwise) {
			System.out.println("\t" + segment);
		}
		System.out.println("clockwise:");
		for (Segment segment : clockwise) {
			System.out.println("\t" + segment);
		}
		
		return new Segment[] {antiClockwise.first(), clockwise.first()};
	}
	
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
			// If the point is colinear to this segment, ignore this segment.
			// As lines are technically 1 dimensional,
			// nothing cast from relativeTo can interact with it anyway.
			// Also prevents problems when relativeTo intersects the segment.
			if (compare(segment, relativeTo) == 0) {
				continue;
			}
			
			// The end point should always be clockwise to the start point.
			if (compare(new Segment(relativeTo, segment.pS), segment.pE) < 0) {
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
		return getFOVLotsOfIfs(centre, segments);
	}
	
	private Collection<Triangle> getFOVLotsOfIfs(Point centre, Collection<Segment> segments) {
		System.out.println("#### getFOV() ####");
		
		Collection<Triangle> triangles = new ArrayList<Triangle>();
		if (segments.isEmpty()) {
			return triangles;
		}
		
		Collection<Segment> normSegs = normaliseSegments(segments, centre);
		System.out.println("Sorted segments (around " + centre + "):");
		for (Segment segment : normSegs) {
			System.out.println("\t" + segment);
		}
		
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
			if (result == 0 && !point1.equals(point2)) {
				// Select the first one.
				result = -1;
			}
			return result;
		});
		
		System.out.println("Sorted points (around " + centre + "):");
		for (Point point : points) {
			System.out.println("\t" + point);
		}
		
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
		Segment[] firstCollisions = getFirstCollisions(new Segment(centre, next), normSegs);
		Point initPoint = firstCollisions[0].pE;
		Point lastPoint = firstCollisions[1].pS;
		Segment currentSegment = firstCollisions[1];
		System.out.println("Starting with initial values. next: " + next + ". initPoint: " + initPoint + ". lastPoint: " + lastPoint + ". currentSegment: " + currentSegment + ".");
		while (iterator.hasNext()) {
			if (currentSegment == null) {
				System.out.println("Null currentSegment.");
				System.out.println("Next: " + next + ", ");
			}
			next = iterator.next();
			if (next.equals(currentSegment.pE)) {
				triangles.add(new Triangle(centre, lastPoint, next));
				
				RayCollision collision = getNextCollision(new Segment(centre, next), normSegs);
				lastPoint = collision.point;
				currentSegment = collision.segment;
				
				System.out.println("End of currentSegment. next: " + next + ". lastPoint: " + lastPoint + ". currentSegment: " + currentSegment + ".");
			} else if (startPointToSegment.containsKey(next)) {
				// TODO: If statement above, otherwise endpoints are tested and sometimes closer than the current segment.
				double[] u = collidesAt(new Segment(centre, next), currentSegment);
				Point pointOnCurrentSegment = getPointAlongSegment(currentSegment, u[1]);
				
				// If next point is closer to centre than current segment.
				if (Utils.lt(distance(centre, next), distance(centre, pointOnCurrentSegment))) {
					triangles.add(new Triangle(centre, lastPoint, pointOnCurrentSegment));
					
					lastPoint = next;
					currentSegment = startPointToSegment.get(next);
					
					System.out.println("Closer segment. next: " + next + ". lastPoint: " + lastPoint + ". currentSegment: " + currentSegment + ".");
				}
			}
		}
		triangles.add(new Triangle(centre, lastPoint, initPoint));
		
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
	
	public enum LineType {
		LINE {
			@Override
			public boolean isOn(double u) {
				return true;
			}

			@Override
			public double min() {
				return Double.NEGATIVE_INFINITY;
			}

			@Override
			public double max() {
				return Double.POSITIVE_INFINITY;
			}
		},
		RAY_EXCLUSIVE {
			@Override
			public boolean isOn(double u) {
				return Utils.gt(u, 0);
			}
			
			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return Double.POSITIVE_INFINITY;
			}
		},
		RAY_INCLUSIVE {
			@Override
			public boolean isOn(double u) {
				return Utils.gte(u, 0);
			}
			
			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return Double.POSITIVE_INFINITY;
			}
		},
		SEGMENT_EXCLUSIVE {
			@Override
			public boolean isOn(double u) {
				return Utils.gt(u, 0) && Utils.lt(u, 1);
			}
			
			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 1;
			}
		},
		SEGMENT_INCLUSIVE {
			@Override
			public boolean isOn(double u) {
				return Utils.gte(u, 0) && Utils.lte(u, 1);
			}
			
			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 1;
			}
		};
		
		public abstract boolean isOn(double u);
		public abstract double min();
		public abstract double max();
	}
	
	public class RayCollision {
		public final Point point;
		public final Segment segment;

		public RayCollision(Point point, Segment segment) {
			this.point = point;
			this.segment = segment;
		}
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
}
