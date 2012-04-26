package com.github.ctrimble.neon.asm.examples;

import java.net.URL;
import java.net.URLStreamHandlerFactory;

import com.github.ctrimble.neon.classloader.NeonURLClassLoader;

public class ExtendingURLClassLoaderAfter
  extends NeonURLClassLoader
{
	public ExtendingURLClassLoaderAfter(URL[] urls) {
		super(urls);
	}

	public ExtendingURLClassLoaderAfter(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ExtendingURLClassLoaderAfter(URL[] urls, ClassLoader parent, URLStreamHandlerFactory handlerFactory) {
		super(urls, parent, handlerFactory);
	}
}
