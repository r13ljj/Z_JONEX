package javaagent.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by xubai on 2018/10/17 上午11:33.
 */
public class AsmTest {

    public static void main(String[] args)throws Exception {
        /*Class<?> clazz = new MyClassLoader().loadClass("javaagent.asm.Test");
        clazz.getMethods()[0].invoke(clazz.newInstance());*/
        String className = "javaagent.asm.Test1";
        byte[] classBytes = createVoidMethod(className, "this is the second asm demo.");
        Class<?> clazz = new MyClassLoader().defineClassForName(className, classBytes);
        Object instance = clazz.newInstance();
        clazz.getMethods()[0].invoke(instance);

    }

    public static ClassWriter createClassWriter(String className){
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //声明一个类，使用JDK1.8版本，public的类，父类是java.lang.Object，没有实现任何接口
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);
        //初始化一个无参构造器
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        //
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        //调用父类构造器
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        //
        constructor.visitInsn(Opcodes.RETURN);
        //最大栈空间和最大本地变量
        constructor.visitMaxs(1, 1);
        //
        constructor.visitEnd();
        return cw;
    }

    public static byte[] createVoidMethod(String className, String message)throws Exception{
        ClassWriter cw = createClassWriter(className.replaceAll(".", "/"));

        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int, float或String型常量值从常量池中推送至栈顶
        runMethod.visitLdcInsn(message);
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1, 1);
        runMethod.visitEnd();
        return cw.toByteArray();
    }

}
