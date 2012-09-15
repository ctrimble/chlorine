package com.github.ctrimble.chlorine.asm.isolated;

import static org.junit.Assert.assertNotSame;

import java.lang.reflect.Method;

import com.github.ctrimble.chlorine.asm.URLLoader;
import com.github.ctrimble.chlorine.asm.Utils;

public class ReflectionURLLoader implements URLLoader
{
	public String loadUrl(String systemId) throws Exception {
		IsolatedExternalDefineClassLoader classLoader = new IsolatedExternalDefineClassLoader(this.getClass().getClassLoader());
		
		String className = "com.github.ctrimble.chlorine.asm.isolated.IsolatedURLLoader";
		byte[] classBytes = Utils.getResourceBytes("/com/github/ctrimble/chlorine/asm/isolated/IsolatedURLLoader.class");

		Method defineMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);		
		defineMethod.setAccessible(true);
		@SuppressWarnings("unchecked")
		Class<? extends URLLoader> simpleClass = (Class<? extends URLLoader>)defineMethod.invoke(classLoader, className, classBytes, 0, classBytes.length);

		assertNotSame("Test failed to load a new class.", simpleClass, this.getClass().getClassLoader().loadClass("com.github.ctrimble.chlorine.asm.isolated.IsolatedURLLoader"));
		
		return simpleClass.newInstance().loadUrl(systemId);
	}
}