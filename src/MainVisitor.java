
import static java.lang.System.out;

import java.util.Scanner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MainVisitor {
	static class Visitor extends CalculatorBaseVisitor<ComplexNumber> {
		 
		 @Override
		 public ComplexNumber visitNumber(CalculatorParser.NumberContext ctx) {
			 return new ComplexNumber(Double.parseDouble(ctx.getText()), 0);
		 }
		 
		 @Override
		 public ComplexNumber visitComplexNumber(CalculatorParser.ComplexNumberContext ctx) {
			 ComplexNumber mod = visit(ctx.number(0));
			 ComplexNumber degree = visit(ctx.number(1));
			 return ComplexNumber.convertPolar(mod.getA(), degree.getA());
		 }
		 @Override
		 public ComplexNumber visitParens(CalculatorParser.ParensContext ctx) {
			 return visit(ctx.expr());
		 }
		 
		 @Override
		 public ComplexNumber visitMulDiv(CalculatorParser.MulDivContext ctx) {
			 ComplexNumber left = visit(ctx.expr(0));
			 ComplexNumber right = visit(ctx.expr(1));
			 if(ctx.op.getType() == CalculatorParser.MULT) {
				 return left.mul(right);
			 } else {
				 return left.div(right);
			 }
		 }
		 
		 @Override
		 public ComplexNumber visitAddSub(CalculatorParser.AddSubContext ctx) {
			 ComplexNumber left = visit(ctx.expr(0));
			 ComplexNumber right = visit(ctx.expr(1));
			 if(ctx.op.getType() == CalculatorParser.PLUS) {
				 return left.add(right);
			 } else {
				 return left.sub(right);
			 }
		 }
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
	        //System.out.println(tokens.getText());

	        CalculatorParser parser = new CalculatorParser(tokens);
	        ParseTree tree = parser.expr(); // parse

	        Visitor eval = new Visitor();
	        System.out.println("");
	        System.out.println(eval.visit(tree).getA());
	        System.out.println(eval.visit(tree).getB());
	 }
}
