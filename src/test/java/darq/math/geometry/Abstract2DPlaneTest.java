package darq.math.geometry;

import darq.math.Const;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Craig.Webster
 */
public class Abstract2DPlaneTest {
	
	public Abstract2DPlaneTest() {
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
	 * Test of collidesAt method, of class Abstract2DPlane.
	 */
	@Test
	public void testCollidesAt() {
		System.out.println("collidesAt");
		Abstract2DPlane instance = new Abstract2DPlaneImpl();
		Segment s1;
		Segment s2;
		double[] exp;
		double[] res;
		String errorFormat = "For test (%s, %s), was expecting %s, but found %s.";
		
		s1 = new Segment(new Point(0, 0), new Point(5, 5));
		s2 = new Segment(new Point(10, 10), new Point(15, 15));
		exp = null;
		res = instance.collidesAt(s1, s2);
		assertArrayEquals(String.format(errorFormat, s1, s2, Arrays.toString(exp), Arrays.toString(res)), exp, res, Const.EPSILON);
	}

	/**
	 * Test of compare method, of class Abstract2DPlane.
	 */
	@Test
	public void testCompare() {
		System.out.println("compare");
		Abstract2DPlane instance = new Abstract2DPlaneImpl();
		Point p;
		Segment s;
		int exp;
		int res;
		String errorFormat = "For test (%s, %s), was expecting %s, but found %s.";
		
		p = new Point(200, 100);
		s = new Segment(new Point(100, 100), new Point(200, 200));
		exp = -1;
		res = instance.compare(p, s);
		assertEquals(String.format(errorFormat, p, s, exp, res), exp, res);
		
		p = new Point(150, 150);
		s = new Segment(new Point(100, 100), new Point(200, 200));
		exp = 0;
		res = instance.compare(p, s);
		assertEquals(String.format(errorFormat, p, s, exp, res), exp, res);
		
		p = new Point(100, 200);
		s = new Segment(new Point(100, 100), new Point(200, 200));
		exp = 1;
		res = instance.compare(p, s);
		assertEquals(String.format(errorFormat, p, s, exp, res), exp, res);
	}

	public class Abstract2DPlaneImpl extends Abstract2DPlane {
		@Override
		public double distance(double yD, double xD) {
			return 0.0;
		}
	}
}
