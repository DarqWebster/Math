package darq.math.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Craig.Webster
 */
public class SpatialHash {
	public final double yCellSize;
	public final double xCellSize;

	private final Map<Coord, Collection<Polygon>> hash;
	
	public SpatialHash(double yCellSize, double xCellSize) {
		this.yCellSize = yCellSize;
		this.xCellSize = xCellSize;
		
		hash = new HashMap<Coord, Collection<Polygon>>();
	}
	
	public Collection<Polygon> getNear(Polygon polygon) {
		Collection<Polygon> polygons = new HashSet<Polygon>();
		
		Collection<Coord> cells = getCells(polygon.boundingBox);
		for (Coord cell : cells) {
			polygons.addAll(hash.get(cell));
		}
		
		return polygons;
	}
	
	public void add(Polygon polygon) {
		Collection<Coord> cells = getCells(polygon.boundingBox);
		for (Coord cell : cells) {
			add(cell, polygon);
		}
	}
	
	public void remove(Polygon polygon) {
		Collection<Coord> cells = getCells(polygon.boundingBox);
		for (Coord cell : cells) {
			remove(cell, polygon);
		}
	}
	
	public void clear() {
		hash.clear();
	}
	
	private void add(Coord cell, Polygon polygon) {
		Collection<Polygon> polygons = hash.get(cell);
		if (polygons == null) {
			polygons = new HashSet<Polygon>();
			hash.put(cell, polygons);
		}
		polygons.add(polygon);
	}
	
	private void remove(Coord cell, Polygon polygon) {
		Collection<Polygon> polygons = hash.get(cell);
		polygons.remove(polygon);
		if (polygons.isEmpty()) {
			hash.remove(cell);
		}
	}
	
	private Coord getCell(Point point) {
		int yCell = (int) (point.y / yCellSize);
		int xCell = (int) (point.x / xCellSize);
		
		return new Coord(yCell, xCell);
	}
	
	private Collection<Coord> getCells(AxisAlignedBoundingBox boundingBox) {
		Coord min = getCell(new Point(boundingBox.yS, boundingBox.xS));
		Coord max = getCell(new Point(boundingBox.yE, boundingBox.xE));
		
		Collection<Coord> cells = new ArrayList<Coord>((max.y - min.y) * (max.x - min.x));
		
		for (int y = min.y; y <= max.y; y++) {
			for (int x = min.x; x <= max.x; x++) {
				cells.add(new Coord(y, x));
			}
		}
		
		return cells;
	}
}
