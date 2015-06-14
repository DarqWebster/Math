package darq.math.geometry;

import darq.math.Utils;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author Craig.Webster
 */
public class Hexagonal2DPlaneTest extends TestCase {
	
	public Hexagonal2DPlaneTest(String testName) {
		super(testName);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testHexant() {
		System.out.println("hexant");
		
		Map<Point, Double> tests = new LinkedHashMap<Point, Double>();
		
		// Centre.
		tests.put(new Point(0, 0), -1D);
		
		// On the line.
		tests.put(new Point( 1,  0), 0D);
		tests.put(new Point( 1,  1), 1D);
		tests.put(new Point( 0,  1), 2D);
		tests.put(new Point(-1,  0), 3D);
		tests.put(new Point(-1, -1), 4D);
		tests.put(new Point( 0, -1), 5D);
		
		// Between lines.
		tests.put(new Point( 4,  0), 0D);
		tests.put(new Point( 4,  1), 0 + 1/4D);
		tests.put(new Point( 4,  2), 0 + 1/2D);
		tests.put(new Point( 4,  3), 0 + 3/4D);
		tests.put(new Point( 4,  4), 1D);
		tests.put(new Point( 3,  4), 1 + 1/4D);
		tests.put(new Point( 2,  4), 1 + 1/2D);
		tests.put(new Point( 1,  4), 1 + 3/4D);
		tests.put(new Point( 0,  4), 2D);
		tests.put(new Point(-1,  3), 2 + 1/4D);
		tests.put(new Point(-2,  2), 2 + 1/2D);
		tests.put(new Point(-3,  1), 2 + 3/4D);
		tests.put(new Point(-4,  0), 3D);
		tests.put(new Point(-4, -1), 3 + 1/4D);
		tests.put(new Point(-4, -2), 3 + 1/2D);
		tests.put(new Point(-4, -3), 3 + 3/4D);
		tests.put(new Point(-4, -4), 4D);
		tests.put(new Point(-3, -4), 4 + 1/4D);
		tests.put(new Point(-2, -4), 4 + 1/2D);
		tests.put(new Point(-1, -4), 4 + 3/4D);
		tests.put(new Point( 0, -4), 5D);
		tests.put(new Point( 1, -3), 5 + 1/4D);
		tests.put(new Point( 2, -2), 5 + 1/2D);
		tests.put(new Point( 3, -1), 5 + 3/4D);
		
		Hexagonal2DPlane instance = new Hexagonal2DPlane();
		for (Map.Entry<Point, Double> test : tests.entrySet()) {
			Point t = test.getKey();
			
			// Uncomment to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			double exp = test.getValue();
			double res = instance.hexant(t.y, t.x);

			String error = "For test " + t + ", was expecting " + exp + ", but found " + res + ".";
			assertTrue(error, Utils.equals(exp, res));
		}
	}
	
	@Test
	public void testRound() {
		System.out.println("round");
		
		Map<Point, Coord[]> tests = new LinkedHashMap<Point, Coord[]>();
		
		// Already round points.
		tests.put(new Point(0, 0), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(1, 0), new Coord[] {
			new Coord(1, 0)
		});
		tests.put(new Point(1, 1), new Coord[] {
			new Coord(1, 1)
		});
		tests.put(new Point(0, 1), new Coord[] {
			new Coord(0, 1)
		});
		tests.put(new Point(-1, 0), new Coord[] {
			new Coord(-1, 0)
		});
		tests.put(new Point(-1, -1), new Coord[] {
			new Coord(-1, -1)
		});
		tests.put(new Point(0, -1), new Coord[] {
			new Coord(0, -1)
		});
		
		// Points within the hex, 0.5 from centre.
		tests.put(new Point(0.5, 0.25), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(0.25, 0.5), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.25, 0.25), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.5, -0.25), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.25, -0.5), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(0.25, -0.25), new Coord[] {
			new Coord(0, 0)
		});
		
		// Points within the hex, but more than 0.5 from centre.
		tests.put(new Point(0.6, 0.3), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(0.3, 0.6), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.3, 0.3), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.6, -0.3), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(-0.3, -0.6), new Coord[] {
			new Coord(0, 0)
		});
		tests.put(new Point(0.3, -0.3), new Coord[] {
			new Coord(0, 0)
		});
		
		// Edges of hexes, not-centres.
		tests.put(new Point(5D/12D, -1D/6D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 0)
		});
		tests.put(new Point(7D/12D, 1D/6D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 0)
		});
		tests.put(new Point(7D/12D, 5D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(5D/12D, 7D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(1D/6D, 7D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, 1)
		});
		tests.put(new Point(-1D/6D, 5D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, 1)
		});
		tests.put(new Point(-5D/12D, 1D/6D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, 0)
		});
		tests.put(new Point(-7D/12D, -1D/6D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, 0)
		});
		tests.put(new Point(-7D/12D, -5D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, -1)
		});
		tests.put(new Point(-5D/12D, -7D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, -1)
		});
		tests.put(new Point(-1D/6D, -7D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, -1)
		});
		tests.put(new Point(1D/6D, -5D/12D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, -1)
		});
		
		// Edges of hexes, centres, 0.5 away from centre.
		tests.put(new Point(0.5, 0), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 0)
		});
		tests.put(new Point(0.5, 0.5), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(0, 0.5), new Coord[] {
			new Coord(0, 0),
			new Coord(0, 1)
		});
		tests.put(new Point(-0.5, 0), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, 0)
		});
		tests.put(new Point(-0.5, -0.5), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, -1)
		});
		tests.put(new Point(0, -0.5), new Coord[] {
			new Coord(0, 0),
			new Coord(0, -1)
		});
		
		// Edges of hexes, centres, 1 away from centre.
		tests.put(new Point(1, 0.5), new Coord[] {
			new Coord(1, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(0.5, 1), new Coord[] {
			new Coord(1, 1),
			new Coord(0, 1)
		});
		tests.put(new Point(-0.5, 0.5), new Coord[] {
			new Coord(0, 1),
			new Coord(-1, 0)
		});
		tests.put(new Point(-1, -0.5), new Coord[] {
			new Coord(-1, 0),
			new Coord(-1, -1)
		});
		tests.put(new Point(-0.5, -1), new Coord[] {
			new Coord(-1, -1),
			new Coord(0, -1)
		});
		tests.put(new Point(0.5, -0.5), new Coord[] {
			new Coord(0, -1),
			new Coord(1, 0)
		});
		
		// Vertexes of hexes.
		tests.put(new Point(2D/3D, 1D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(1D/3D, 2D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(1, 1),
			new Coord(0, 1)
		});
		tests.put(new Point(-1D/3D, 1D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, 1),
			new Coord(-1, 0)
		});
		tests.put(new Point(-2D/3D, -1D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, 0),
			new Coord(-1, -1)
		});
		tests.put(new Point(-1D/3D, -2D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(-1, -1),
			new Coord(0, -1)
		});
		tests.put(new Point(1D/3D, -1D/3D), new Coord[] {
			new Coord(0, 0),
			new Coord(0, -1),
			new Coord(1, 0)
		});
		
		// Random.
		tests.put(new Point(0.7, 0.3), new Coord[] {
			new Coord(1, 0)
		});
		tests.put(new Point(0.7, 0.35), new Coord[] {
			new Coord(1, 0),
			new Coord(1, 1)
		});
		tests.put(new Point(0.4, 0.4), new Coord[] { // Same error margin on > 1 axis, only one rounded Coord.
			new Coord(0, 0)
		});
		
		Hexagonal2DPlane instance = new Hexagonal2DPlane();
		for (Map.Entry<Point, Coord[]> test : tests.entrySet()) {
			Point t = test.getKey();
			
			// Uncomment and add breakpoint to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			Coord[] exp = test.getValue();
			Coord[] res = instance.round(t.y, t.x);

			String error = "For test " + t + ", was expecting " + Arrays.deepToString(exp) + ", but found " + Arrays.deepToString(res) + ".";
			assertEquals(error, exp.length, res.length);
			for (int i = 0; i < exp.length; i++) {
				assertTrue(error, exp[i].equals(res[i]));
			}
		}
		
		// Wiggle wiggle wiggle.
		for (Map.Entry<Point, Coord[]> test : tests.entrySet()) {
			double w = 0.000000000001;
			
			Point p = test.getKey();
			Point[] wiggle = new Point[] {
				p,
				new Point(p.y + w, p.x    ),
				new Point(p.y - w, p.x    ),
				new Point(p.y    , p.x + w),
				new Point(p.y    , p.x - w),
				new Point(p.y + w, p.x + w),
				new Point(p.y + w, p.x - w),
				new Point(p.y - w, p.x + w),
				new Point(p.y - w, p.x - w)
			};
			
			// Uncomment and add breakpoint to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			Coord[] exp = test.getValue();
			for (Point t : wiggle) {
				Coord[] res = instance.round(t.y, t.x);
				String error = "For test " + t + ", was expecting " + Arrays.deepToString(exp) + ", but found " + Arrays.deepToString(res) + ".";
				assertEquals(error, exp.length, res.length);
				for (int i = 0; i < exp.length; i++) {
					assertTrue(error, exp[i].equals(res[i]));
				}
			}
		}
	}
	
	@Test
	public void testGetPointByHexant() {
		System.out.println("getPointByHexant");
		
		Map<double[], Point> tests = new LinkedHashMap<double[], Point>();
		
		// On the line.
		tests.put(new double[] {1D, 0D}, new Point( 1,  0));
		tests.put(new double[] {1D, 1D}, new Point( 1,  1));
		tests.put(new double[] {1D, 2D}, new Point( 0,  1));
		tests.put(new double[] {1D, 3D}, new Point(-1,  0));
		tests.put(new double[] {1D, 4D}, new Point(-1, -1));
		tests.put(new double[] {1D, 5D}, new Point( 0, -1));

		// Between lines.
		tests.put(new double[] {4D, 0D}, new Point( 4,  0));
		tests.put(new double[] {4D, 0 + 1/4D}, new Point( 4,  1));
		tests.put(new double[] {4D, 0 + 1/2D}, new Point( 4,  2));
		tests.put(new double[] {4D, 0 + 3/4D}, new Point( 4,  3));
		tests.put(new double[] {4D, 1D}, new Point( 4,  4));
		tests.put(new double[] {4D, 1 + 1/4D}, new Point( 3,  4));
		tests.put(new double[] {4D, 1 + 1/2D}, new Point( 2,  4));
		tests.put(new double[] {4D, 1 + 3/4D}, new Point( 1,  4));
		tests.put(new double[] {4D, 2D}, new Point( 0,  4));
		tests.put(new double[] {4D, 2 + 1/4D}, new Point(-1,  3));
		tests.put(new double[] {4D, 2 + 1/2D}, new Point(-2,  2));
		tests.put(new double[] {4D, 2 + 3/4D}, new Point(-3,  1));
		tests.put(new double[] {4D, 3D}, new Point(-4,  0));
		tests.put(new double[] {4D, 3 + 1/4D}, new Point(-4, -1));
		tests.put(new double[] {4D, 3 + 1/2D}, new Point(-4, -2));
		tests.put(new double[] {4D, 3 + 3/4D}, new Point(-4, -3));
		tests.put(new double[] {4D, 4D}, new Point(-4, -4));
		tests.put(new double[] {4D, 4 + 1/4D}, new Point(-3, -4));
		tests.put(new double[] {4D, 4 + 1/2D}, new Point(-2, -4));
		tests.put(new double[] {4D, 4 + 3/4D}, new Point(-1, -4));
		tests.put(new double[] {4D, 5D}, new Point( 0, -4));
		tests.put(new double[] {4D, 5 + 1/4D}, new Point( 1, -3));
		tests.put(new double[] {4D, 5 + 1/2D}, new Point( 2, -2));
		tests.put(new double[] {4D, 5 + 3/4D}, new Point( 3, -1));
		
		Hexagonal2DPlane instance = new Hexagonal2DPlane();
		for (Map.Entry<double[], Point> test : tests.entrySet()) {
			double[] t = test.getKey();
			
			// Uncomment to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			Point exp = test.getValue();
			Point res = instance.getPointByHexant(t[0], t[1]);

			String error = "For test (" + t[0] + ", " + t[1] + "), was expecting " + exp + ", but found " + res + ".";
			assertTrue(error, Utils.equals(exp, res));
		}
	}
	
	@Test
	public void testContainsPolygonPoint() {
		System.out.println("contains(Polygon, Point)");
		
		Hexagonal2DPlane instance = new Hexagonal2DPlane();
		Polygon polygon;
		Point point;
		int exp;
		int res;
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(150, 150);
		System.out.println("(" + polygon + ", " + point + "); // Centre.");
		exp = 1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(150, 100);
		System.out.println("(" + polygon + ", " + point + "); // On left vertical side segment.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(150, 200);
		System.out.println("(" + polygon + ", " + point + "); // On right vertical side segment.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(100, 150);
		System.out.println("(" + polygon + ", " + point + "); // On bottom horizontal side segment.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(200, 150);
		System.out.println("(" + polygon + ", " + point + "); // On top horizontal side segment.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(100, 100);
		System.out.println("(" + polygon + ", " + point + "); // On bottom left corner.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(200, 100);
		System.out.println("(" + polygon + ", " + point + "); // On top left corner.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(200, 200);
		System.out.println("(" + polygon + ", " + point + "); // On top right corner.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(100, 200);
		System.out.println("(" + polygon + ", " + point + "); // On bottom right corner.");
		exp = 0;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(50, 50);
		System.out.println("(" + polygon + ", " + point + "); // Outside, bottom left.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(250, 250);
		System.out.println("(" + polygon + ", " + point + "); // Outside, top right.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(50, 150);
		System.out.println("(" + polygon + ", " + point + "); // Outside, below.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(50, 250);
		System.out.println("(" + polygon + ", " + point + "); // Outside, above.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(150, 50);
		System.out.println("(" + polygon + ", " + point + "); // Outside, left.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(150, 250);
		System.out.println("(" + polygon + ", " + point + "); // Outside, right.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(50, 100);
		System.out.println("(" + polygon + ", " + point + "); // Outside, below, inline with left side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(250, 100);
		System.out.println("(" + polygon + ", " + point + "); // Outside, above, inline with left side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(50, 200);
		System.out.println("(" + polygon + ", " + point + "); // Outside, below, inline with right side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(250, 200);
		System.out.println("(" + polygon + ", " + point + "); // Outside, above, inline with right side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(100, 50);
		System.out.println("(" + polygon + ", " + point + "); // Outside, left, inline with bottom side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(200, 50);
		System.out.println("(" + polygon + ", " + point + "); // Outside, left, inline with top side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(100, 250);
		System.out.println("(" + polygon + ", " + point + "); // Outside, right, inline with bottom side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
		
		polygon = new Polygon(
				new Point(100, 100),
				new Point(200, 100),
				new Point(200, 200),
				new Point(100, 200)
		);
		point = new Point(200, 250);
		System.out.println("(" + polygon + ", " + point + "); // Outside, right, inline with top side.");
		exp = -1;
		res = instance.contains(polygon, point);
		assertEquals(exp, res);
	}
	
	@Test
	public void testContainsSegmentPoint() {
		System.out.println("contains(Segment, Point)");
		
		Hexagonal2DPlane instance = new Hexagonal2DPlane();
		Segment segment;
		Point point;
		boolean exp;
		boolean res;
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(50, 50);
		System.out.println("(" + segment + ", " + point + "); // Colinear, but before.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(100, 100);
		System.out.println("(" + segment + ", " + point + "); // Start.");
		exp = true;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(125, 125);
		System.out.println("(" + segment + ", " + point + "); // Quarter.");
		exp = true;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(150, 150);
		System.out.println("(" + segment + ", " + point + "); // Half.");
		exp = true;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(175, 175);
		System.out.println("(" + segment + ", " + point + "); // Three-quarter.");
		exp = true;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(200, 200);
		System.out.println("(" + segment + ", " + point + "); // End.");
		exp = true;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(250, 250);
		System.out.println("(" + segment + ", " + point + "); // Colinear, but after.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(75, 125);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to start.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(125, 75);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to start.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(125, 175);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to centre.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(175, 125);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to centre.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(175, 225);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to end.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
		
		segment = new Segment(new Point(100, 100), new Point(200, 200));
		point = new Point(225, 175);
		System.out.println("(" + segment + ", " + point + "); // Off, perpendicular to end.");
		exp = false;
		res = instance.contains(segment, point);
		assertEquals(exp, res);
	}
}