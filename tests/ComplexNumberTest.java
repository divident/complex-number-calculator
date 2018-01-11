import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ComplexNumberTest {
	private ComplexNumber a;
	private ComplexNumber b;

	@Before
	public void setUp() throws Exception {
		a = new ComplexNumber(1.0, 8.0);
		b = new ComplexNumber(2.0, 3.0);
	}

	@Test
	public void testAdd() {
		ComplexNumber c = a.add(b);
		assertEquals(3.0, c.getA(), 1e-9);
		assertEquals(11.0, c.getB(), 1e-9);
	}

	@Test
	public void testSub() {
		ComplexNumber c = a.sub(b);
		assertEquals(-1.0, c.getA(), 1e-9);
		assertEquals(5.0, c.getB(), 1e-9);
	}

	@Test
	public void testMul() {
		ComplexNumber c = a.mul(b);
		assertEquals(-22.0, c.getA(), 1e-9);
		assertEquals(19.0, c.getB(), 1e-9);

	}

	@Test
	public void testMod() {
		double c = a.mod();
		assertEquals(8.062257748, c, 1e-9);
	}

	@Test
	public void testCon() {
		ComplexNumber c = a.con();
		assertEquals(1.0, c.getA(), 1e-9);
		assertEquals(-8.0, c.getB(), 1e-9);
	}

	@Test
	public void testDiv() {
		ComplexNumber c = a.div(b);
		assertEquals(2.0, c.getA(), 1e-9);
		assertEquals(1.0, c.getB(), 1e-9);
	}

	@Test
	public void testConvertPolar() {
		ComplexNumber c = ComplexNumber.convertPolar(5.0, 0.5*Math.PI);
		assertEquals(0.0, c.getA(), 1e-9);
		assertEquals(5.0, c.getB(), 1e-9);
	}
	
	@Test
	public void testSqrt() {
	  ComplexNumber c = new ComplexNumber(1, 1);
	  c = c.sqrt();
	  assertEquals(1.0986841134678098, c.getA(), 1e-9);
	  assertEquals(0.4550898605622273, c.getB(), 1e-9);
	  
	  ComplexNumber a = new ComplexNumber(4, 0);
	  a = a.sqrt();
	  assertEquals(2.0, a.getA(), 1e-9);
	  assertEquals(0.0, a.getB(), 1e-9);
	  
	  a.setA(-1);
	  a.setB(0);
	  a = a.sqrt();
	  assertEquals(0.0, a.getA(), 1e-9);
	  assertEquals(1.0, a.getB(), 1e-9);
	  
	  a.setA(0);
	  a.setB(8.0);
	  a = a.sqrt();
	  
	  assertEquals(2.0, a.getA(), 1e-9);
	  assertEquals(2.0, a.getB(), 1e-9);
	  
	}
	
}
