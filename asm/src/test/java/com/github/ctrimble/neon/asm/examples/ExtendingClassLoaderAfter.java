package com.github.ctrimble.neon.asm.examples;

import com.github.ctrimble.neon.classloader.NeonClassLoader;

public class ExtendingClassLoaderAfter
  extends NeonClassLoader
{
	public ExtendingClassLoaderAfter() {
		super();
	}

	public ExtendingClassLoaderAfter(ClassLoader parent) {
		super(parent);
	}
}
