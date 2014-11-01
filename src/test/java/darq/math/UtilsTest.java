/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package darq.math;

import darq.math.geometry.Line;
import darq.math.geometry.Point;
import junit.framework.TestCase;

/**
 *
 * @author Craig.Webster
 */
public class UtilsTest extends TestCase {
	
	public UtilsTest(String testName) {
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
	
	/**
	 * Test of equals method, of class Utils.
	 */
	public void testEqualsDouble() {
		System.out.println("equals (double)");
		double d1;
		double d2;
		boolean exp;
		boolean res;
		
		d1 = 1D/3D;
		d2 = 1-(2D/3D);
		exp = true;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = 7D/12D;
		d2 = 1-(5D/12D);
		exp = true;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
	}
}