package com.github.ctrimble.chlorine.asm;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ReflectionAdapter extends ClassVisitor {

	public ReflectionAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      return new ReflectionVisitor(super.visitMethod(access, name, desc, signature, exceptions));
	}

	public class ReflectionVisitor
      extends MethodVisitor
    {
	public ReflectionVisitor(MethodVisitor mv) {
		super(ASM4, mv);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		if( opcode == INVOKEVIRTUAL && "java/lang/reflect/Method".equals(owner) && "invoke".equals(name) && "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;".equals(desc)) {
			mv.visitMethodInsn(INVOKESTATIC, "com/github/ctrimble/chlorine/asm/ReflectionInterceptor", "invoke", "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
		}
		else super.visitMethodInsn(opcode, owner, name, desc);

	}

}
}
