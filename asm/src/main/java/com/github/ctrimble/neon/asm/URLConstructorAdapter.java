package com.github.ctrimble.neon.asm;

import org.objectweb.asm.ClassVisitor;
import static org.objectweb.asm.Opcodes.ASM4;

/**
 * A class visitor that translates all calls to new java.net.URL(...) into calls to com.github.ctrimble.neon.service.URLFactory.newURL(...).
 *
 * @author Christian Trimble
 */
public class URLConstructorAdapter
  extends ClassVisitor
{
	public URLConstructorAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}
}
