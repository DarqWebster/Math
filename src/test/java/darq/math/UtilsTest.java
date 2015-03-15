/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package darq.math;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Test;

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
	
	@Test
	public void testRoundOptEps() {
		System.out.println("roundOptEps");
		double value;
		long exp;
		long res;
		
		value = 0D;
		exp = 0L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.1D;
		exp = 0L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.9D;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		double w = 0.000000000001;
		
		value = 0D + w;
		exp = 0L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0D - w;
		exp = 0L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D + w;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D - w;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D + w;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D - w;
		exp = 1L;
		res = Utils.roundOptEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
	}
	
	@Test
	public void testRoundPesEps() {
		System.out.println("roundPesEps");
		double value;
		long exp;
		long res;
		
		value = 0D;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D;
		exp = 1L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.1D;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.9D;
		exp = 1L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		double w = 0.000000000001;
		
		value = 0D + w;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0D - w;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D + w;
		exp = 1L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 1D - w;
		exp = 1L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D + w;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		value = 0.5D - w;
		exp = 0L;
		res = Utils.roundPesEps(value);
		assertEquals("For test (" + value + "), was expecting " + exp + " but found " + res + ".", exp, res);
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
		
		d1 = Double.NaN;
		d2 = Double.NaN;
		exp = true;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NaN;
		d2 = 0;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NaN;
		d2 = 128D;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.POSITIVE_INFINITY;
		d2 = Double.POSITIVE_INFINITY;
		exp = true;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NEGATIVE_INFINITY;
		d2 = Double.NEGATIVE_INFINITY;
		exp = true;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.POSITIVE_INFINITY;
		d2 = Double.NEGATIVE_INFINITY;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.POSITIVE_INFINITY;
		d2 = 0;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NEGATIVE_INFINITY;
		d2 = 0;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NaN;
		d2 = Double.POSITIVE_INFINITY;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
		
		d1 = Double.NaN;
		d2 = Double.NEGATIVE_INFINITY;
		exp = false;
		res = Utils.equals(d1, d2);
		assertEquals("For test (" + d1 + " == " + d2 + "), was expecting " + exp + " but found " + res + ".", exp, res);
	}
	
	public void testClosest() {
		System.out.println("closest");
		double reference;
		double[] values;
		double exp;
		double res;
		
		reference = 0;
		values = new double[] {
			
		};
		exp = Double.NaN;
		res = Utils.closest(reference, values);
		assertEquals("For test (" + reference + ", " + Arrays.toString(values) + "), was expecting " + exp + " but found " + res + ".", exp, res);
	}
}