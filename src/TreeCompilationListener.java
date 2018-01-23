import java.text.ParseException;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TreeCompilationListener extends CalculatorBaseListener {
	private final MethodVisitor _mv;
	private int _currentTreeDepth, _maxTreeDepth;

	public int getTreeDepth() {
		return this._maxTreeDepth;
	}

	public TreeCompilationListener(MethodVisitor mv) {
		this._mv = mv;
	}

	@Override
	public void exitRectComplex(CalculatorParser.RectComplexContext ctx) {
		this._mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "ComplexNumber", "<init>", "(DD)V", false);
	}

	@Override
	public void enterRectComplex(CalculatorParser.RectComplexContext ctx) {
		this._currentTreeDepth++;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);
		this._mv.visitTypeInsn(Opcodes.NEW, "ComplexNumber");
		this._mv.visitInsn(Opcodes.DUP);
	}

	@Override
	public void exitTrigComplex(CalculatorParser.TrigComplexContext ctx) {
		this._currentTreeDepth++;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);
		this._mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ComplexNumber", "convertPolar", "(DDD)LComplexNumber;", false);
	}

	@Override
	public void enterPositiveNumber(CalculatorParser.PositiveNumberContext ctx) {
		this._currentTreeDepth++;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);
		if (this._currentTreeDepth > this._maxTreeDepth)
			this._maxTreeDepth = this._currentTreeDepth;

		try {
			this._mv.visitLdcInsn(TreeMain.parse(ctx.getText()));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Number is not of the double type.");
		}
	}

	@Override
	public void enterNegativeNumber(CalculatorParser.NegativeNumberContext ctx) {
		this._currentTreeDepth++;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);

		try {
			this._mv.visitLdcInsn(TreeMain.parse(ctx.getText()));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Number is not of the double type.");
		}
	}

	@Override
	public void enterReNumber(CalculatorParser.ReNumberContext ctx) {
		this._currentTreeDepth+=2;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);
		this._mv.visitTypeInsn(Opcodes.NEW, "ComplexNumber");
		this._mv.visitInsn(Opcodes.DUP);
	}

	@Override
	public void enterImgNumber(CalculatorParser.ImgNumberContext ctx) {
		this._currentTreeDepth+=2;
		this._maxTreeDepth = Math.max(this._currentTreeDepth, this._maxTreeDepth);
		this._mv.visitTypeInsn(Opcodes.NEW, "ComplexNumber");
		this._mv.visitInsn(Opcodes.DUP);
		try {
			this._mv.visitLdcInsn(TreeMain.parse("0.0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void exitImgNumber(CalculatorParser.ImgNumberContext ctx) {
		this._mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "ComplexNumber", "<init>", "(DD)V", false);
	}

	@Override
	public void exitReNumber(CalculatorParser.ReNumberContext ctx) {
		try {
			this._mv.visitLdcInsn(TreeMain.parse("0.0"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this._mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "ComplexNumber", "<init>", "(DD)V", false);
	}
	
	
	@Override public void exitExpComplex(CalculatorParser.ExpComplexContext ctx) { 
		this._mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ComplexNumber", "convertPolar", "(DD)LComplexNumber;", false);
	}
	
	@Override
	public void exitAddSub(CalculatorParser.AddSubContext ctx) {
		if (ctx.op.getType() == CalculatorParser.PLUS) {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "add", "(LComplexNumber;)LComplexNumber;",
					false);
		} else {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "sub", "(LComplexNumber;)LComplexNumber;",
					false);
		}
		this._currentTreeDepth--;
	}
	
	@Override
	public void exitMulDiv(CalculatorParser.MulDivContext ctx) {
		if(ctx.op.getType() == CalculatorParser.MULT) {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "mul", "(LComplexNumber;)LComplexNumber;",
					false);
		} else {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "div", "(LComplexNumber;)LComplexNumber;",
					false);
		}
		this._currentTreeDepth--;
	}
	
	@Override 
	public void exitSqrt(CalculatorParser.SqrtContext ctx) {
		this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "sqrt", "()LComplexNumber;",
				false);
		this._currentTreeDepth--;
	}
	
	@Override
	public void exitOpComplex(CalculatorParser.OpComplexContext ctx) {
		if(ctx.op.getType() == CalculatorParser.IM) {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "imPart", "()LComplexNumber;",
					false);
		} else {
			this._mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "ComplexNumber", "rePart", "()LComplexNumber;",
					false);
		}
		this._currentTreeDepth--;
	}
}
