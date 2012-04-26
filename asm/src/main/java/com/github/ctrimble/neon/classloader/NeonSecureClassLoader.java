package com.github.ctrimble.neon.classloader;

import java.security.SecureClassLoader;

public class NeonSecureClassLoader extends SecureClassLoader {

	public NeonSecureClassLoader() {
	}

	public NeonSecureClassLoader(ClassLoader parent) {
		super(parent);
	}

}
