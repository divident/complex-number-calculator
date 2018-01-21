import static org.junit.Assert.*;

import org.junit.Test;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class CalculatorTest {

	ParseTree tree;
	CalculatorParser parser;

	void setup(String input) {
		CharStream charStream = CharStreams.fromString(input);
		CalculatorLexer lexer = new CalculatorLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		parser = new CalculatorParser(tokens);

	}

	@Test
	public void testNumberDouble() {
		setup("1.2319");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(1.2319, 0);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testNumberInt() {
		setup("1");
		assertEquals(CalculatorParser.INT, parser.getCurrentToken().getType());
	}

	@Test
	public void testValidComplexNumber() {
		setup("2 + 1)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(3, 0);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
		
		setup("3i + 2i + 1");
		tree = parser.expr();
		number = new ComplexNumber(1, 5);
		eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
		
		
		setup("2.5(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		number = ComplexNumber.convertPolar(2.5, 1.047197);
		eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
		
		setup("2.5(cos1.047197+isin1.047197)");
		tree = parser.expr();
		eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));

	}

	@Test(expected = ArithmeticException.class)
	public void testInvalidComplexNumber() {
		setup("2.5(cos(3.047197)+isin(1.047197))");
		tree = parser.expr();
		TreeVisitor eval = new TreeVisitor();
		eval.visit(tree);
	}
	
	@Test(expected = ArithmeticException.class)
	public void testInvalidMod() {
		setup("-2.5(cos(3.047197)+isin(1.047197))");
		tree = parser.expr();
		TreeVisitor eval = new TreeVisitor();
		eval.visit(tree);
	}
	

	@Test
	public void testOperationMul() {
		setup("1(cos(1.047197)+isin(1.047197)) * 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.mul(number);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationDiv() {
		setup("1(cos(1.047197)+isin(1.047197)) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationAdd() {
		setup("1(cos(1.047197)+isin(1.047197)) + 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.add(number);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationSub() {
		setup("1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationPriority() {
		setup("1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197)) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		res = number.sub(res);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationBrackets() {
		setup("(1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197))) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		res = res.div(number);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testSqrtOperation() {
		setup("sqrt(1(cos(1.047197)+isin(1.047197)))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.sqrt();
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testSqrtCompOperation() {
		setup("sqrt(1(cos(1.047197)+isin(1.047197))) + 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.sqrt();
		number = number.add(ComplexNumber.convertPolar(1, 1.047197));
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testSqrtExpresion() {
		setup("sqrt(1(cos(1.047197)+isin(1.047197)) * 1(cos(1.047197)+isin(1.047197)))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.mul(number);
		number = number.sqrt();
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumber() {
		setup("(-5 - i7)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(-5, -7);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumberAddOperation() {
		setup("(5 + 7i) + (5 + 7i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(10, 14);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumberMinusOperation() {
		setup("(5.5 + 7i) - (2.5+ 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(3, 5);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumberMulOperation() {
		setup("(5 + 7i) * (2.0 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(-4, 24);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumberDivOperation() {
		setup("(5 + 7i) / (2 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(3, 0.5);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(number.equals(eval.visit(tree)));
	}

	@Test
	public void testValidRectComplexNumberSqrtOperation() {
		setup("sqrt(2.0 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(1.5537739740300374, 0.6435942529055826);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testValidImgNumber() {
		setup("4i");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(0, 4);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
		setup("i-4");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), -1 * eval.visit(tree).getB(), 1e-9);
		setup("4*i");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
		setup("i*-4");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), -1 * eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testValidImgNumberOper() {
		setup("4i * (2 - 2i)");
		tree = parser.expr();
		TreeVisitor eval = new TreeVisitor();
		ComplexNumber a = new ComplexNumber(0, 4);
		ComplexNumber b = new ComplexNumber(2, -2);
		ComplexNumber c = a.mul(b);
		assertTrue(c.equals(eval.visit(tree)));
		setup("4i + (2 - 2i)");
		c = a.add(b);
		tree = parser.expr();
		assertTrue(c.equals(eval.visit(tree)));
		setup("sqrt(4i) + 2i *(-2 -2i)");
		ComplexNumber d = new ComplexNumber(0, 2);
		b.setA(-1 * b.getA());
		c = a.sqrt();
		d = d.mul(b);
		c = c.add(d);
		tree = parser.expr();
		assertTrue(c.equals(eval.visit(tree)));
	}

	@Test
	public void testExpValidComplexNumber() {
		setup("1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);

	}

	@Test
	public void testExpOperationMul() {
		setup("1*e^(i*1.047197) * 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.mul(number);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationDiv() {
		setup("1*e^(i*1.047197) / 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationAdd() {
		setup("1*e^(i*1.047197) + 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.add(number);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationSub() {
		setup("1*e^(i*1.047197) - 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationPriority() {
		setup("1*e^(i*1.047197) - 1*e^(i*1.047197) / 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		res = number.sub(res);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationBrackets() {
		setup("(1*e^(i*1.047197) - 1*e^(i*1.047197)) / 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		res = res.div(number);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testSingleReAndIm() {
		setup("1 + 3i / 2 + 2 * 3i");
		tree = parser.expr();
		ComplexNumber n = new ComplexNumber(1, 7.5);
		TreeVisitor eval = new TreeVisitor();
		assertTrue(n.equals(eval.visit(tree)));
	}

	@Test
	public void testOperatorRe() {
		setup("re(1+ 3i + 2 + 4i)");
		tree = parser.expr();
		ComplexNumber n = new ComplexNumber(3, 0);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(n.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(n.getB(), eval.visit(tree).getB(), 1e-9);
		setup("RE(1 + 3i + 7i)");
		n = new ComplexNumber(1, 0);
		tree = parser.expr();
		assertEquals(n.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(n.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testOperatorIm() {
		setup("im((1*e^(i*1.047197) - 1*e^(i*1.047197)) / 1*e^(i*1.047197))");
		tree = parser.expr();
		ComplexNumber n = ComplexNumber.convertPolar(0, 1.047197);
		n.setA(0);
		TreeVisitor eval = new TreeVisitor();
		assertEquals(n.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(n.getB(), eval.visit(tree).getB(), 1e-9);
		setup("IM((1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197))) / 1(cos(1.047197)+isin(1.047197)))");
		n = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = n.sub(n);
		res = res.div(n);
		res.setA(0);
		tree = parser.expr();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}
	
}
