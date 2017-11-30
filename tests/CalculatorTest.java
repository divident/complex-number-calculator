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
}
