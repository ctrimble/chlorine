package com.github.ctrimble.neon.asm.examples;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.ctrimble.neon.asm.ReflectionInterceptor;

public class ReflectionAfter {
	  public void invokeDefine() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		  Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
		  method.setAccessible(true);
		  ReflectionInterceptor.invoke(method, this.getClass().getClassLoader(), "Name", new byte[] { 0 }, 0, 1); 
	  }
}
