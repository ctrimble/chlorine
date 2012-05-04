package com.github.ctrimble.neon.asm.examples;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class ReflectionBefore {
  public void invokeDefine() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	  Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
	  method.setAccessible(true);
	  method.invoke(this.getClass().getClassLoader(), "Name", new byte[] { 0 }, 0, 1); 
  }
}
