package com.github.ctrimble.neon.classloader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

public interface NeonClassDefiner {
	Class<?> neonDefineClass(byte[] b, int off, int len)
			throws ClassFormatError;

	Class<?> neonDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError;
	
	Class<?> neonDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError;

	Class<?> neonDefineClass(String name, ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError;
}
