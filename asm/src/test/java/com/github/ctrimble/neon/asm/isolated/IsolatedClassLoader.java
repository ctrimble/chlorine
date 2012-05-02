package com.github.ctrimble.neon.asm.isolated;

import static com.github.ctrimble.neon.asm.Utils.getResourceBytes;

public class IsolatedClassLoader
	  extends ClassLoader
	  {
		public IsolatedClassLoader( ClassLoader parent ) {
			super(parent);
		}
		 public Class<?> loadClass(String className, boolean resolve)
		   throws ClassNotFoundException
		 {
			 Class<?> clazz = null;
			 if( className.startsWith("com.github.ctrimble.neon.asm.isolated") ) {
				 clazz = findLoadedClass(className);
				 if( clazz == null ) clazz = findClass(className);
				 if( clazz != null && resolve ) resolveClass(clazz);
			 }
			 else {
				 clazz = super.loadClass(className, resolve);
			 }
			 
			 return clazz;
		 }
		 public Class<?> findClass(String className)
		   throws ClassNotFoundException
		 {
			 if( className.startsWith("com.github.ctrimble.neon.asm.isolated") ) {
				 try {
				   byte[] classBytes = getResourceBytes("/"+className.replaceAll("\\.", "/")+".class");
				   return defineClass(className, classBytes, 0, classBytes.length);
				 }
				 catch (Exception e ) {
					 throw new ClassNotFoundException(className, e);
				 }
			 }
			 else throw new ClassNotFoundException(className);
		 }
	  }

