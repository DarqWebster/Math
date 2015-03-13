package darq.math.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Craig.Webster
 */
public class Polygon {
	public final List<Point> points;
	public final List<Segment> segments;
	public final AxisAlignedBoundingBox boundingBox;
	
	public Polygon(Point... points) {
		this.points = Collections.unmodifiableList(Arrays.asList(points));
		
		List<Segment> temp = new ArrayList<Segment>(points.length);
		for (int i = 0; i < points.length - 1; i++) {
			temp.add(new Segment(points[i], points[i + 1]));
		}
		if (points.length > 2) {
			temp.add(new Segment(points[points.length - 1], points[0]));
		}
		this.segments = Collections.unmodifiableList(temp);
		
		boundingBox = getBoundingBox(points);
	}
	
	private static AxisAlignedBoundingBox getBoundingBox(Point... points) {
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		
		for (Point point : points) {
			if (point.y < minY) {
				minY = point.y;
			}
			if (point.y > maxY) {
				maxY = point.y;
			}
			if (point.x < minX) {
				minX = point.x;
			}
			if (point.x > maxX) {
				maxX = point.x;
			}
		}
		
		return new AxisAlignedBoundingBox(minY, minX, maxY, maxX);
	}

	@Override
	public String toString() {
		return "Polygon{points=" + points + '}';
	}
}
