package com.github.ctrimble.neon.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class NeonURLClassLoader extends URLClassLoader {

	public NeonURLClassLoader(URL[] urls) {
		super(urls);
	}

	public NeonURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public NeonURLClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

}
