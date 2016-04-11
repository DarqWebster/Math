package darq.math.geometry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author darq
 */
public class SquareEuclidean2DPlaneTest {
	
	public SquareEuclidean2DPlaneTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	/**
	 * Test of distanceBetween method, of class Abstract2DPlane.
	 */
	@Test
	public void testCollides() {
		System.out.println("collides");
		SquareEuclidean2DPlane instance = new SquareEuclidean2DPlane();
		Polygon p1;
		Polygon p2;
		Segment exp;
		Segment res;
		String errorFormat = "For test (%s, %s), was expecting %s, but found %s.";

		p1 = new Polygon(
				new Point(0D, 0D),
				new Point(10D, 0D),
				new Point(10D, 10D),
				new Point(0D, 10D)
		);
		p2 = new Polygon(
				new Point(0D, 20D),
				new Point(10D, 20D),
				new Point(10D, 30D),
				new Point(0D, 30D)
		);
		exp = null;
		res = instance.collides(p1, p2);
		assertEquals(String.format(errorFormat, p1, p2, exp, res), exp, res);
		
		p1 = new Polygon(
				new Point(0D, 0D),
				new Point(10D, 0D),
				new Point(10D, 10D),
				new Point(0D, 10D)
		);
		p2 = new Polygon(
				new Point(0D, 5D),
				new Point(10D, 5D),
				new Point(10D, 15D),
				new Point(0D, 15D)
		);
		exp = new Segment(new Point(0, 0), new Point(0, 5));
		res = instance.collides(p1, p2);
		assertEquals(String.format(errorFormat, p1, p2, exp, res), exp, res);
		
		// Reverse construction of one polygon.
		p1 = new Polygon(
				new Point(0D, 0D),
				new Point(0D, 10D),
				new Point(10D, 10D),
				new Point(10D, 0D)
		);
		p2 = new Polygon(
				new Point(0D, 5D),
				new Point(10D, 5D),
				new Point(10D, 15D),
				new Point(0D, 15D)
		);
		exp = new Segment(new Point(0, 0), new Point(0, 5));
		res = instance.collides(p1, p2);
		assertEquals(String.format(errorFormat, p1, p2, exp, res), exp, res);
		
		// Reverse constrution of both ploygons.
		p1 = new Polygon(
				new Point(0D, 0D),
				new Point(0D, 10D),
				new Point(10D, 10D),
				new Point(10D, 0D)
		);
		p2 = new Polygon(
				new Point(0D, 5D),
				new Point(0D, 15D),
				new Point(10D, 15D),
				new Point(10D, 5D)
		);
		exp = new Segment(new Point(0, 0), new Point(0, 5));
		res = instance.collides(p1, p2);
		assertEquals(String.format(errorFormat, p1, p2, exp, res), exp, res);
		
		p1 = new Polygon(
				new Point(0D, 0D),
				new Point(10D, 0D),
				new Point(10D, 10D),
				new Point(0D, 10D)
		);
		p2 = new Polygon(
				new Point(0D, 10D),
				new Point(10D, 10D),
				new Point(10D, 20D),
				new Point(0D, 20D)
		);
		exp = new Segment(new Point(0, 0), new Point(0, 0));
		res = instance.collides(p1, p2);
		assertEquals(String.format(errorFormat, p1, p2, exp, res), exp, res);
	}
}
