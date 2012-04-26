package com.github.ctrimble.neon.asm.examples;



public class ExtendingClassLoaderBefore
  extends ClassLoader
{
	public ExtendingClassLoaderBefore() {
		super();
	}

	public ExtendingClassLoaderBefore(ClassLoader parent) {
		super(parent);
	}
}
