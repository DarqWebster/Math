package darq.math.geometry;

import darq.math.Utils;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Ignore;
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
		
		for (Map.Entry<Point, Double> test : tests.entrySet()) {
			Point t = test.getKey();
			
			// Uncomment to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			double exp = test.getValue();
			double res = Hexagonal2DPlane.hexant(t.y, t.x);

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
		
		for (Map.Entry<Point, Coord[]> test : tests.entrySet()) {
			Point t = test.getKey();
			
			// Uncomment to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			Coord[] exp = test.getValue();
			Coord[] res = Hexagonal2DPlane.round(t.y, t.x);

			String error = "For test " + t + ", was expecting " + Arrays.deepToString(exp) + ", but found " + Arrays.deepToString(res) + ".";
			assertEquals(error, exp.length, res.length);
			for (int i = 0; i < exp.length; i++) {
				assertTrue(error, exp[i].equals(res[i]));
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
		
		for (Map.Entry<double[], Point> test : tests.entrySet()) {
			double[] t = test.getKey();
			
			// Uncomment to debug.
//			if (t.equals(new Point(0D, 0D))) {
//				System.out.println("Debug...");
//			}
			
			Point exp = test.getValue();
			Point res = Hexagonal2DPlane.getPointByHexant(t[0], t[1]);

			String error = "For test (" + t[0] + ", " + t[1] + "), was expecting " + exp + ", but found " + res + ".";
			assertTrue(error, Utils.equals(exp, res));
		}
	}
	
//	@Ignore @Test
//	public void testGetLine() {
//		System.out.println("getLine");
//		
//		int y1;
//		int x1;
//		int y2;
//		int x2;
//		
//		Map<int[], Point[]> cases = new LinkedHashMap<int[], Point[]>();
//		
//		// Base case.
//		cases.put(new int[] {0, 0, 0, 0}, new Point[] {
//			new Point(0, 0)
//		});
//		
//		// Same signs.
//		cases.put(new int[] {0, 0, 5, 0}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 0),
//			new Point(2, 0),
//			new Point(3, 0),
//			new Point(4, 0),
//			new Point(5, 0)
//		});
//		cases.put(new int[] {0, 0, 5, 1}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 0),
//			new Point(2, 0),
//			new Point(3, 1),
//			new Point(4, 1),
//			new Point(5, 1)
//		});
//		cases.put(new int[] {0, 0, 5, 2}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 0),
//			new Point(2, 1),
//			new Point(3, 1),
//			new Point(4, 2),
//			new Point(5, 2)
//		});
//		cases.put(new int[] {0, 0, 5, 3}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 1),
//			new Point(2, 1),
//			new Point(3, 2),
//			new Point(4, 2),
//			new Point(5, 3)
//		});
//		cases.put(new int[] {0, 0, 5, 4}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 1),
//			new Point(2, 2),
//			new Point(3, 2),
//			new Point(4, 3),
//			new Point(5, 4)
//		});
//		cases.put(new int[] {0, 0, 5, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 1),
//			new Point(2, 2),
//			new Point(3, 3),
//			new Point(4, 4),
//			new Point(5, 5)
//		});
//		cases.put(new int[] {0, 0, 4, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 1),
//			new Point(2, 2),
//			new Point(2, 3),
//			new Point(3, 4),
//			new Point(4, 5)
//		});
//		cases.put(new int[] {0, 0, 3, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 1),
//			new Point(1, 2),
//			new Point(2, 3),
//			new Point(2, 4),
//			new Point(3, 5)
//		});
//		cases.put(new int[] {0, 0, 2, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(0, 1),
//			new Point(1, 2),
//			new Point(1, 3),
//			new Point(2, 4),
//			new Point(2, 5)
//		});
//		cases.put(new int[] {0, 0, 1, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(0, 1),
//			new Point(0, 2),
//			new Point(1, 3),
//			new Point(1, 4),
//			new Point(1, 5)
//		});
//		cases.put(new int[] {0, 0, 0, 5}, new Point[] {
//			new Point(0, 0),
//			new Point(0, 1),
//			new Point(0, 2),
//			new Point(0, 3),
//			new Point(0, 4),
//			new Point(0, 5)
//		});
////		cases.put(new int[] {0, 0, 2, 1}, new Point[] {
////			new Point(0, 0),
////			new Point(1, 1),
////			new Point(2, 1),
////		});
////		cases.put(new int[] {0, 0, -1, -2}, new Point[] {
////			new Point(0, 0),
////			new Point(1, 1),
////			new Point(1, 2),
////		});
////		cases.put(new int[] {0, 0, 1, -1}, new Point[] {
////			new Point(0, 0),
////			new Point(0, -1),
////			new Point(1, -1),
////		});
//		
//		// Differing signs.
//		cases.put(new int[] {0, 0, 4, -1}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 0),
//			new Point(2, 0),
//			new Point(2, -1),
//			new Point(3, -1),
//			new Point(4, -1)
//		});
//		cases.put(new int[] {0, 0, 3, -2}, new Point[] {
//			new Point(0, 0),
//			new Point(1, 0),
//			new Point(1, -1),
//			new Point(2, -1),
//			new Point(2, -2),
//			new Point(3, -2)
//		});
//		cases.put(new int[] {0, 0, 2, -3}, new Point[] {
//			new Point(0, 0),
//			new Point(0, -1),
//			new Point(1, -1),
//			new Point(1, -2),
//			new Point(2, -2),
//			new Point(2, -3)
//		});
//		cases.put(new int[] {0, 0, 1, -4}, new Point[] {
//			new Point(0, 0),
//			new Point(0, -1),
//			new Point(0, -2),
//			new Point(1, -2),
//			new Point(1, -3),
//			new Point(1, -4)
//		});
//		
//		// Replicate each test case for each quadrant.
//		int[][] quadrants = {
//			{+1, +1},
//			{-1, -1}
//		};
//		
//		for (Map.Entry<int[], Point[]> entry : cases.entrySet()) {
//			// Actual test.
//			int[] key = entry.getKey();
//			Point[] value = entry.getValue();
//			
//			for (int[] quadrant : quadrants) {
//				int endY = key[2] * quadrant[0];
//				int endX = key[3] * quadrant[1];
//				
//				System.out.print("\tfrom (" + key[0] + ", " + key[1] + ") to (" + endY + ", " + endX + ")");
//				Point[] res = Hexagonal2DPlane.getLine(key[0], key[1], endY, endX, 1, value.length);
//				System.out.print(" res: " + Arrays.deepToString(res));
//				Point[] rev = Hexagonal2DPlane.getLine(endY, endX, key[0], key[1], 1, value.length);
//				System.out.print(" rev: " + Arrays.deepToString(rev));
//				System.out.println();
//
//				for (int i = 0; i < value.length; i++) {
//					Point exp = new Point(value[i].y * quadrant[0], value[i].x * quadrant[1]);
//					assertTrue(exp + " expected, " + res[i] + " found.", exp.equals(res[i]));
//					//assertTrue("Solution is non-symmetrical.", exp.equals(rev[rev.length - i - 1]));
//				}
//			}
//		}
//	}
}