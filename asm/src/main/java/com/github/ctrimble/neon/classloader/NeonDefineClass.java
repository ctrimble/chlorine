package com.github.ctrimble.neon.classloader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

import com.github.ctrimble.neon.asm.ClassByteLoader;
import com.github.ctrimble.neon.asm.ClassTransformer;

public abstract class NeonDefineClass {
	private ClassByteLoader loader;
	public NeonDefineClass( ClassLoader cl ) {
		loader = new ClassByteLoader(cl);
	}
	
	protected Class<?> defineClass(byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return parentDefineClass(newBytes, 0, newBytes.length);
	}

	protected Class<?> defineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length);
	}

	protected Class<?> defineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name,  newBytes, 0, newBytes.length, protectionDomain);
	}

	protected Class<?> defineClass(String name, ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, protectionDomain);
	}
	
	public abstract Class<?> parentDefineClass(byte[] b, int off, int len)
			throws ClassFormatError;

	public abstract Class<?> parentDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError;
	
	public abstract Class<?> parentDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain)
			throws ClassFormatError;
	
	public abstract Class<?> parentDefineClass(String name, ByteBuffer b, ProtectionDomain protectionDomain)
			throws ClassFormatError;
}
