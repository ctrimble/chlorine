package com.github.ctrimble.chlorine.classloader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

import com.github.ctrimble.chlorine.asm.ClassByteLoader;
import com.github.ctrimble.chlorine.asm.ClassTransformer;

/**
 * Chlorine's replacement for java.lang.ClassLoader references.  In cases where ClassLoader is instantiated, an
 * instance of this class will be used instead.  In cases where non java/javax packages extend java.lang.ClassLoader,
 * this class loader will be injected into the class hierarchy.
 * 
 * @author Christian Trimble
 *
 */
public class ChlorineClassLoader extends ClassLoader implements ChlorineClassDefiner {

	private ClassByteLoader loader;
	public ChlorineClassLoader() {
		loader = new ClassByteLoader(this);
	}

	public ChlorineClassLoader(ClassLoader parent) {
		super(parent);
		loader = new ClassByteLoader(this);
	}

	@SuppressWarnings("deprecation")
	public Class<?> chlorineDefineClass(byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(newBytes, 0, newBytes.length);
	}

	public Class<?> chlorineDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length);
	}

	public Class<?> chlorineDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name,  newBytes, 0, newBytes.length, protectionDomain);
	}

	public Class<?> chlorineDefineClass(String name, ByteBuffer b,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, protectionDomain);
	}
}
