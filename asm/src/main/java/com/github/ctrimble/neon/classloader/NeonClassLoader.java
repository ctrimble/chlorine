package com.github.ctrimble.neon.classloader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

import com.github.ctrimble.neon.asm.ClassByteLoader;
import com.github.ctrimble.neon.asm.ClassTransformer;

public class NeonClassLoader extends ClassLoader implements NeonClassDefiner {

	private ClassByteLoader loader;
	public NeonClassLoader() {
		loader = new ClassByteLoader(this);
	}

	public NeonClassLoader(ClassLoader parent) {
		super(parent);
		loader = new ClassByteLoader(this);
	}

	@SuppressWarnings("deprecation")
	public Class<?> neonDefineClass(byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(newBytes, 0, newBytes.length);
	}

	public Class<?> neonDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length);
	}

	public Class<?> neonDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name,  newBytes, 0, newBytes.length, protectionDomain);
	}

	public Class<?> neonDefineClass(String name, ByteBuffer b,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, protectionDomain);
	}
}
