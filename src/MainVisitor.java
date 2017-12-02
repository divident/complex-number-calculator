
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
		 public ComplexNumber visitTrigComplex(CalculatorParser.TrigComplexContext ctx) {
			 ComplexNumber mod = visit(ctx.number(0));
			 ComplexNumber degree = visit(ctx.number(1));
			 ComplexNumber degree2 = visit(ctx.number(2));
			 if(degree.equals(degree2)) {
				 return ComplexNumber.convertPolar(mod.getA(), degree.getA());
			 } else {
				 throw new ArithmeticException("Degrees must be equal");
			 }
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
		 
		 @Override
		 public ComplexNumber visitSqrt(CalculatorParser.SqrtContext ctx) {
			 ComplexNumber c = visit(ctx.sqrtComplex().expr());
			 return c.sqrt();
		 }
		 
		 @Override
		 public ComplexNumber visitExpComplex(CalculatorParser.ExpComplexContext ctx) {
			 return ComplexNumber.convertPolar(
					 visit(ctx.realNumber()).getA(),
			         visit(ctx.expdegree()).getA()
			         );
		 }
		 @Override
		 public ComplexNumber visitRectComplex(CalculatorParser.RectComplexContext ctx) {
			 ComplexNumber ret = new ComplexNumber(
					 visit(ctx.realNumber(0)).getA(),
					 visit(ctx.realNumber(1)).getA()
					 );
			 if(ctx.sig.getType() == CalculatorParser.MINUS) {
				 ret.setB(ret.getB()*-1);
			 } 
			 return ret;
		 }
		 
		 @Override
		 public ComplexNumber visitPositiveNumber(CalculatorParser.PositiveNumberContext ctx) {
			 return visit(ctx.number());
		 }
		 
		 @Override
		 public ComplexNumber visitNegativeNumber(CalculatorParser.NegativeNumberContext ctx) {
			 ComplexNumber c = visit(ctx.number());
			 c.setA(-1*c.getA());
			 return c;
		 }
		 
		 @Override
		 public ComplexNumber visitImgNumber(CalculatorParser.ImgNumberContext ctx) {
			 ComplexNumber c = visit(ctx.realNumber());
			 c.setB(c.getA());
			 c.setA(0);
			 return c;
		 }
		 
		 @Override
		 public ComplexNumber visitOpComplex(CalculatorParser.OpComplexContext ctx) {
			 ComplexNumber c = visit(ctx.expr());
			 if(ctx.op.getType() == CalculatorParser.IM) {
				 c.setA(0);
			 } else {
				 c.setB(0);
			 }
			 return c;
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
	        parser.setBuildParseTree(true);
	        ParseTree tree = parser.expr();
	        int errors = parser.getNumberOfSyntaxErrors();

	        out.println("\nNumber of syntax errors: " + errors);

	        if (0 == errors) {
	        try {
		        Visitor eval = new Visitor();
		        out.print(eval.visit(tree).toString());	
	        } catch(Exception e) {
	        	out.print(e.getMessage());
	        }
	        }
	 }
}
