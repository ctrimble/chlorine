package com.github.ctrimble.chlorine.asm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.ctrimble.chlorine.URLFactory;
import com.github.ctrimble.chlorine.classloader.NeonClassLoader;

public class NestedClassLoaderTest {
	@BeforeClass
	public static void registerLabel() {
		URLFactory.setURLStreamHandlerFactory(new TestingURLStreamHandlerFactory());
	}
	
	@Test
	public void testExtendingClassLoader() throws Exception {  
		// get the loader class.
		IsolatedNeonClassLoader isolatedCl = new IsolatedNeonClassLoader(this.getClass().getClassLoader());
		@SuppressWarnings("unchecked")
		Class<? extends URLLoader> urlLoaderClass = (Class<? extends URLLoader>)isolatedCl.loadClass("com.github.ctrimble.chlorine.asm.isolated.NestedURLLoader");

		// create a new loader.
		URLLoader loader = urlLoaderClass.newInstance();
		
		// this will cause the creation of a nested class loader, from which the source URL will be created.
		String value = loader.loadUrl("label://domain/path");
		
		assertEquals("TESTING=/path", value);
	}
	
	public static class IsolatedNeonClassLoader
	  extends NeonClassLoader
	  {
		public IsolatedNeonClassLoader( ClassLoader parent ) {
			super(parent);
		}
		 public Class<?> loadClass(String className, boolean resolve)
		   throws ClassNotFoundException
		 {
			 Class<?> clazz = null;
			 if( className.startsWith("com.github.ctrimble.chlorine.asm.isolated") ) {
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
			 if( className.startsWith("com.github.ctrimble.chlorine.asm.isolated") ) {
				 try {
				   byte[] classBytes = Utils.getResourceBytes("/"+className.replaceAll("\\.", "/")+".class");
				   return chlorineDefineClass(className, classBytes, 0, classBytes.length);
				 }
				 catch (Exception e ) {
					 throw new ClassNotFoundException(className, e);
				 }
			 }
			 else throw new ClassNotFoundException(className);
		 }
	  }
}
