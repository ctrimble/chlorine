package com.github.ctrimble.neon.asm.examples;

import com.github.ctrimble.neon.classloader.NeonSecureClassLoader;

public class ExtendingSecureClassLoaderAfter
  extends NeonSecureClassLoader
{
	public ExtendingSecureClassLoaderAfter() {
		super();
	}

	public ExtendingSecureClassLoaderAfter(ClassLoader parent) {
		super(parent);
	}
}
