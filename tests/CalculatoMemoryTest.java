import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;


public class CalculatoMemoryTest {
	ParseTree tree;
	CalculatorParser parser;
	Map<String, ComplexNumber> memory = new HashMap<String, ComplexNumber>();
	
	void setup(String input) {
		CharStream charStream = CharStreams.fromString(input);
		CalculatorLexer lexer = new CalculatorLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		parser = new CalculatorParser(tokens);

	}
	
	@Test
	public void testAdd() {
		memory.put("c", new ComplexNumber(2, 0));
		memory.put("b", new ComplexNumber(3, 2));
		setup("c + b * 2");
		tree = parser.expr();
		ComplexNumber number = new ComplexNumber(8, 4);
		TreeVisitor eval = new TreeVisitor();
		eval.variables.putAll(memory);
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testMul() {
		memory.put("b", new ComplexNumber(2, 2));
		setup("(2 + 3i)*b");
		tree = parser.expr();
		ComplexNumber number = (new ComplexNumber(2, 3)).mul(new ComplexNumber(2, 2));
		TreeVisitor eval = new TreeVisitor();
		eval.variables.putAll(memory);
		assertTrue(number.equals(eval.visit(tree)));
	}
	
	@Test
	public void testAllComplexForm() {
		memory.put("b", new ComplexNumber(3.2, 2.1));
		memory.put("a", new ComplexNumber(5.2, 9.1));
		setup("sqrt(1(cos(1.047197) + isin(1.047197))) * b / (-5 - i7) + 1*e^(i*1.047197) - a");
		tree = parser.expr();
		ComplexNumber number = (ComplexNumber.convertPolar(1, 1.047197).sqrt().mul(new ComplexNumber(3.2, 2.1))
				.div(new ComplexNumber(-5, -7)).add(ComplexNumber.convertPolar(1, 1.047197)).sub(new ComplexNumber(5.2, 9.1)));
		TreeVisitor eval = new TreeVisitor();
		eval.variables.putAll(memory);
		assertTrue(number.equals(eval.visit(tree)));
		
	}
}
