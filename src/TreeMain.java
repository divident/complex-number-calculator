import static java.lang.System.out;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class TreeMain {
	private static final DecimalFormat _format = new DecimalFormat();
	private TreeVisitor eval;
	private ParseTree tree;
	
	static {
		DecimalFormatSymbols _symbols = new DecimalFormatSymbols();
		_symbols.setDecimalSeparator('.');
		_symbols.setGroupingSeparator(Character.MIN_VALUE);
		_format.setDecimalFormatSymbols(_symbols);
	}

	static double parse(String text) throws ParseException {
		return _format.parse(text).doubleValue();
	}
	
	public TreeMain() {
		eval = new TreeVisitor();
	}
	
	public String eval(String expr) {
		
		CharStream charStream = CharStreams.fromString(expr);
		CalculatorLexer lexer = new CalculatorLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// System.out.println(tokens.getText());

		CalculatorParser parser = new CalculatorParser(tokens);
		parser.setBuildParseTree(true);
		tree = parser.expr();
		int errors = parser.getNumberOfSyntaxErrors();

		if (0 == errors) {
			try {
				return eval.visit(tree).toString();
			} catch (Exception e) {
				//e.printStackTrace();
				return e.getMessage();
			}

		} else {
			return "\nNumber of syntax errors: " + errors;
		}
	}

	public void compile() {
		compile(tree, "target/classes/CompilationClass.class");
	}

	public void compile(ParseTree tree, String classPath) {
		if (null == tree)
			throw new NullPointerException("parse tree cannot be null.");

		try {
			Path path = Paths.get(classPath);
			byte[] bytes = Files.readAllBytes(path);
			ClassReader cr = new ClassReader(bytes);
			// ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS); // no need to
			// calculate visitMaxs(int maxStack, int maxLocals) arguments
			ClassWriter cw = new ClassWriter(cr, 0);
			cr.accept(new CompilationClassVisitor(cw, tree), 0);
			bytes = cw.toByteArray();
			Files.write(path, bytes);
		} catch (IOException e) {
			out.println("CompilationClass.class not found.");
		}
	}

	public TreeVisitor getEval() {
		return eval;
	}
}
