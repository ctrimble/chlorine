package com.github.ctrimble.neon.asm.examples;

import java.security.SecureClassLoader;

public class ExtendingSecureClassLoaderBefore
  extends SecureClassLoader
{
	public ExtendingSecureClassLoaderBefore() {
		super();
	}

	public ExtendingSecureClassLoaderBefore(ClassLoader parent) {
		super(parent);
	}
}
