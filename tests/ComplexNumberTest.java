import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComplexNumberTest {
	private ComplexNumber a;
	private ComplexNumber b;

	@BeforeEach
	void setUp() throws Exception {
		a = new ComplexNumber(1.0, 8.0);
		b = new ComplexNumber(2.0, 3.0);
	}

	@Test
	void testAdd() {
		ComplexNumber c = a.add(b);
		assertEquals(3.0, c.getA(), 1e-9);
		assertEquals(11.0, c.getB(), 1e-9);
	}

	@Test
	void testSub() {
		ComplexNumber c = a.sub(b);
		assertEquals(-1.0, c.getA(), 1e-9);
		assertEquals(5.0, c.getB(), 1e-9);
	}

	@Test
	void testMul() {
		ComplexNumber c = a.mul(b);
		assertEquals(-22.0, c.getA(), 1e-9);
		assertEquals(19.0, c.getB(), 1e-9);

	}

	@Test
	void testMod() {
		double c = a.mod();
		assertEquals(8.062257748, c, 1e-9);
	}

	@Test
	void testCon() {
		ComplexNumber c = a.con();
		assertEquals(1.0, c.getA(), 1e-9);
		assertEquals(-8.0, c.getB(), 1e-9);
	}

	@Test
	void testDiv() {
		ComplexNumber c = a.div(b);
		assertEquals(2.0, c.getA(), 1e-9);
		assertEquals(1.0, c.getB(), 1e-9);
	}

	@Test
	void testConvertPolar() {
		ComplexNumber c = ComplexNumber.convertPolar(5.0, 0.5*Math.PI);
		assertEquals(0.0, c.getA(), 1e-9);
		assertEquals(5.0, c.getB(), 1e-9);
	}
	
	@Test
	void testSqrt() {
	  ComplexNumber c = new ComplexNumber(1, 1);
	  c = c.sqrt();
	  assertEquals(1.0986841134678098, c.getA());
	  assertEquals(0.4550898605622273, c.getB());
	}

}
