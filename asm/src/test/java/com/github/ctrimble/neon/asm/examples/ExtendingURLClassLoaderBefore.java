package com.github.ctrimble.neon.asm.examples;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;


public class ExtendingURLClassLoaderBefore
  extends URLClassLoader
{
	public ExtendingURLClassLoaderBefore(URL[] urls) {
		super(urls);
	}

	public ExtendingURLClassLoaderBefore(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ExtendingURLClassLoaderBefore(URL[] urls, ClassLoader parent, URLStreamHandlerFactory handlerFactory) {
		super(urls, parent, handlerFactory);
	}
}
