package ru.otus.pro;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.*;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("ru/otus/pro/Calculator")) {
                    return addProxyMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(ASM5, cw) {

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new LogMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), descriptor);
            }
        };

        cr.accept(cv, ASM5);

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("ProxyCalculator.class")) {
            fos.write(finalClass);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }
}
