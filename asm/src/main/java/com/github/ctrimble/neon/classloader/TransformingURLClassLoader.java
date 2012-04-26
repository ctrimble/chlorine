package com.github.ctrimble.neon.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class TransformingURLClassLoader extends URLClassLoader {

	public TransformingURLClassLoader(URL[] urls) {
		super(urls);
	}

	public TransformingURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	public TransformingURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory handlerFactory) {
		super(urls, parent, handlerFactory);
	}
}
