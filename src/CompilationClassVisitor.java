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
        if ("execute".equalsIgnoreCase(name) && (access == Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC) && null != mv)
        {
            mv.visitCode();
            ParseTreeWalker walker = new ParseTreeWalker();
            TreeCompilationListener listener = new TreeCompilationListener(mv);
            walker.walk(listener, this._parseTree);
            mv.visitInsn(Opcodes.ARETURN);
            mv.visitMaxs(2 * listener.getTreeDepth(), 0); // double -> 2 *
            return null;
        }
        else return mv;
    }
}
