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


public class Main {
	private static final DecimalFormat _format = new DecimalFormat();

	static {
		DecimalFormatSymbols _symbols = new DecimalFormatSymbols();
		_symbols.setDecimalSeparator('.');
		_symbols.setGroupingSeparator(Character.MIN_VALUE);
		_format.setDecimalFormatSymbols(_symbols);
	}

	static double parse(String text) throws ParseException {
		return _format.parse(text).doubleValue();
	}

	public static void main(String[] args) throws Exception {
		String input = null;
		out.println("Key in the input string:");
		try (Scanner reader = new Scanner(System.in)) {
			input = reader.nextLine();
		}
		CharStream charStream = CharStreams.fromString(input);
		CalculatorLexer lexer = new CalculatorLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// System.out.println(tokens.getText());

		CalculatorParser parser = new CalculatorParser(tokens);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.expr();
		int errors = parser.getNumberOfSyntaxErrors();

		out.println("\nNumber of syntax errors: " + errors);

		if (0 == errors) {
			try {
				TreeVisitor eval = new TreeVisitor();
				out.print(eval.visit(tree).toString());
			} catch (Exception e) {
				out.print(e.getMessage());
			}
			// Synteza
			if (args.length > 0)
				compile(tree, args[0]);
			else
				compile(tree);
		}
	}

	private static void compile(ParseTree tree) {
		compile(tree, "target/classes/CompilationClass.class");
	}

	private static void compile(ParseTree tree, String classPath) {
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
}
