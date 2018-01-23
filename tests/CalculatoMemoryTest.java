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
}
