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
		assertEquals(CalculatorParser.DOUBLE, parser.getCurrentToken().getType());
	}

	@Test
	public void testNumberInt() {
		setup("1");
		assertEquals(CalculatorParser.INT, parser.getCurrentToken().getType());
	}

	@Test
	public void testValidComplexNumber() {
		setup("1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));

	}

	@Test
	public void testOperationMul() {
		setup("1(cos(1.047197)+isin(1.047197)) * 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.mul(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}

	@Test
	public void testOperationDiv() {
		setup("1(cos(1.047197)+isin(1.047197)) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}
	
	@Test
	public void testOperationAdd() {
		setup("1(cos(1.047197)+isin(1.047197)) + 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.add(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}
	
	@Test
	public void testOperationSub() {
		setup("1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}
	
	@Test
	public void testOperationPriority() {
		setup("1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		res = number.sub(res);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}
	
	@Test
	public void testOperationBrackets() {
		setup("(1(cos(1.047197)+isin(1.047197)) - 1(cos(1.047197)+isin(1.047197))) / 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		res = res.div(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(res.equals(eval.visit(tree)));
	}
	
	@Test
	public void testSqrtOperation() {
		setup("sqrt(1(cos(1.047197)+isin(1.047197)))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.sqrt();
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
	}
	
    @Test 
    public void testSqrtCompOperation() {
    	setup("sqrt(1(cos(1.047197)+isin(1.047197))) + 1(cos(1.047197)+isin(1.047197))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.sqrt();
		number = number.add(ComplexNumber.convertPolar(1, 1.047197));
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
    }
    
    @Test
    public void testSqrtExpresion() {
    	setup("sqrt(1(cos(1.047197)+isin(1.047197)) * 1(cos(1.047197)+isin(1.047197)))");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		number = number.mul(number);
		number = number.sqrt();
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
    }
    
	@Test
	public void testValidRectComplexNumber() {
		setup("(-5 - i7)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(-5, -7);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
        assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testValidRectComplexNumberAddOperation() {
		setup("(5 + 7i) + (5 + 7i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(10, 14);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testValidRectComplexNumberMinusOperation() {
		setup("(5.5 + 7i) - (2.5+ 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(3, 5);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testValidRectComplexNumberMulOperation() {
		setup("(5 + 7i) * (2.0 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(-4, 24);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testValidRectComplexNumberDivOperation() {
		setup("(5 + 7i) / (2 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(3, 0.5);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testValidRectComplexNumberSqrtOperation() {
		setup("sqrt(2.0 + 2i)");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(1.5537739740300374, 0.6435942529055826);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
	}
	
	@Test
	public void testValidImgNumber() {
		setup("4i");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(0, 4);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
		setup("i-4");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), -1*eval.visit(tree).getB(), 1e-9);
		setup("4*i");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);
		setup("i*-4");
		tree = parser.expr();
		assertEquals(number.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), -1*eval.visit(tree).getB(), 1e-9);
	}
	
	@Test
	public void testValidImgNumberOper() {
		setup("4i * (2 - 2i)");
		tree = parser.expr();
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
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
		b.setA(-1*b.getA());
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
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(number.getA(),eval.visit(tree).getA(), 1e-9);
		assertEquals(number.getB(), eval.visit(tree).getB(), 1e-9);

	}

	@Test
	public void testExpOperationMul() {
		setup("1*e^(i*1.047197) * 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.mul(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}

	@Test
	public void testExpOperationDiv() {
		setup("1*e^(i*1.047197) / 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.div(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}
	
	@Test
	public void testExpOperationAdd() {
		setup("1*e^(i*1.047197) + 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.add(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}
	
	@Test
	public void testExpOperationSub() {
		setup("1*e^(i*1.047197) - 1*e^(i*1.047197)");
		tree = parser.expr();
		ComplexNumber number = ComplexNumber.convertPolar(1, 1.047197);
		ComplexNumber res = number.sub(number);
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
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
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
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
		MainVisitor.Visitor eval = new MainVisitor.Visitor();
		assertEquals(res.getA(), eval.visit(tree).getA(), 1e-9);
		assertEquals(res.getB(), eval.visit(tree).getB(), 1e-9);
	}
}
