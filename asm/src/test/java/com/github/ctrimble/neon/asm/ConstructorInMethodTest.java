package com.github.ctrimble.neon.asm;

import static org.junit.Assert.*;
import static com.github.ctrimble.neon.asm.Utils.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.ctrimble.neon.service.NetIsolationService;

public class ConstructorInMethodTest {
	
	@BeforeClass
	public static void registerLabel() {
		NetIsolationService.setURLStreamHandlerFactory(new TestingURLStreamHandlerFactory());
	}

	@Test
	public void singleMethod() throws Exception {
		TranslatingClassLoader classLoader = new TranslatingClassLoader(this.getClass().getClassLoader());
		byte[] simpleClassBytes = getResourceBytes("SimpleURLLoader.class");
		Class<?> simpleClass = classLoader.translateAndDefineClass("com.github.ctrimble.neon.asm.SimpleURLLoader", simpleClassBytes);
		URLLoader loader = (URLLoader)simpleClass.newInstance();
		String value = loader.loadUrl("label://host/path");
		assertEquals("TESTING=/path", value);
	}

	public static class TranslatingClassLoader
	  extends ClassLoader
	  {
		public TranslatingClassLoader(ClassLoader parent) {
			super(parent);
		}
		
		public Class<?> translateAndDefineClass(String name, byte[] source)
          throws ClassFormatError
        {
		  byte[] bt = ClassTransformer.transform(source);
          return defineClass(name, bt, 0, bt.length);
        }
	  }
}
