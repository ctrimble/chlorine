/**
 *   Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.ctrimble.neon.asm;

import static org.junit.Assert.*;
import static com.github.ctrimble.neon.asm.Utils.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.ctrimble.neon.URLFactory;

public class ConstructorInMethodTest {
	
	@BeforeClass
	public static void registerLabel() {
		URLFactory.setURLStreamHandlerFactory(new TestingURLStreamHandlerFactory());
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
