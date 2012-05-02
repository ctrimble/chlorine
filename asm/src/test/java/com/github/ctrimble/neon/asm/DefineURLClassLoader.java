package com.github.ctrimble.neon.asm;

import java.net.URL;
import java.net.URLClassLoader;

public class DefineURLClassLoader 
	  extends URLClassLoader
	  {
		public DefineURLClassLoader(ClassLoader parent) {
			super(new URL[] {}, parent);
		}

		public Class<?> publicDefineClass(String name, byte[] b) {
			return this.defineClass(name, b, 0, b.length);
		}
}
