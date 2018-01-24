import org.antlr.v4.runtime.tree.*;
import org.objectweb.asm.*;

public final class CompilationClassVisitor extends ClassVisitor {
	private final ParseTree _parseTree;

	public CompilationClassVisitor(ClassVisitor visitor, ParseTree tree) {
		super(Opcodes.ASM5, visitor);
		if (null == tree)
			throw new IllegalArgumentException("parse tree cannot be null.");

		this._parseTree = tree;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if ("main".equalsIgnoreCase(name) && (access == Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC) && null != mv) {
			mv.visitCode();
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			ParseTreeWalker walker = new ParseTreeWalker();
			TreeCompilationListener listener = new TreeCompilationListener(mv);
			walker.walk(listener, this._parseTree);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(2 * listener.getTreeDepth(), 1); // double -> 2 *
			return null;
		} else
			return mv;
	}
}
