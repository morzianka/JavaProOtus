package ru.otus.pro;

import org.objectweb.asm.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;

/**
 * @author Shabunina Anita
 */
public class LogMethodVisitor extends MethodVisitor {

    private final String descriptor;
    private boolean logAnnotation;

    public LogMethodVisitor(MethodVisitor mv, String descriptor) {
        super(ASM5, mv);
        this.descriptor = descriptor;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (logAnnotation) {
            logAnnotation = false;
            visit();
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lru/otus/pro/Log;")) {
            logAnnotation = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    private void visit() {
        Handle handle = createHandle();
        visitInsn(POP);
        visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        int i = 1;
        for (Type arg : Type.getArgumentTypes(descriptor)) {
            if (arg.getDescriptor().equals(INT_TYPE.getDescriptor())) {
                visitVarInsn(ILOAD, i);
            } else if (arg.getDescriptor().equals(LONG_TYPE.getDescriptor())) {
                visitVarInsn(LLOAD, i);
            } else if (arg.getDescriptor().equals(DOUBLE_TYPE.getDescriptor())) {
                visitVarInsn(DLOAD, i);
            } else {
                visitVarInsn(ALOAD, i);
            }
            i++;
        }
        String desc = descriptor.replaceFirst("\\).", "\\)");
        visitInvokeDynamicInsn("makeConcatWithConstants", desc + "Ljava/lang/String;", handle, "executed method: calculation with params: " + "\u0001 ".repeat(i - 1));

        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        visitInsn(BIPUSH);

        visitInsn(RETURN);
    }

    private Handle createHandle() {
        return new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);
    }
}
