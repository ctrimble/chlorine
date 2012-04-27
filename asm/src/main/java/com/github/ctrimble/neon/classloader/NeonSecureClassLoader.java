package com.github.ctrimble.neon.classloader;

import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;

import com.github.ctrimble.neon.asm.ClassByteLoader;
import com.github.ctrimble.neon.asm.ClassTransformer;

public class NeonSecureClassLoader extends SecureClassLoader implements NeonSecureClassDefiner {

	private ClassByteLoader loader;
	public NeonSecureClassLoader() {
		loader = new ClassByteLoader(this);
	}

	public NeonSecureClassLoader(ClassLoader parent) {
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

	public Class<?> neonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length, cs);
	}

	public Class<?> neonDefineClass(String name, ByteBuffer b, CodeSource cs) {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, cs);
	}

}
