package com.github.ctrimble.neon.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.Opcodes.ASM4;

public class ClassTransformer {
  public static byte[] transform( byte[] source ) {
	  ClassReader cr = new ClassReader(source);
	  ClassWriter cw = new ClassWriter(cr, 0);
	  URLConstructorAdapter adapter = new URLConstructorAdapter(cw);
	  cr.accept(adapter, 0);
	  return cw.toByteArray(); 
  }
  
  public static class URLConstructorAdapter
    extends ClassVisitor
    {
	  public URLConstructorAdapter( ClassVisitor next ) {
		  super(ASM4, next);
	  }
	  
	  
    }
}
