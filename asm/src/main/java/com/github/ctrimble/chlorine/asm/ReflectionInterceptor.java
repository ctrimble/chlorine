package com.github.ctrimble.chlorine.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.Arrays;

/**
 * A set of static methods that are injected into reflection based code.
 * 
 * @author Christian Trimble
 */
public class ReflectionInterceptor {
	
	public static final Class<?>[] DEFINE_PARAMS_BYTE = new Class<?>[] { byte[].class, int.class, int.class };
	public static final Class<?>[] DEFINE_PARAMS_NAME_BYTE = new Class<?>[] { String.class, byte[].class, int.class, int.class };
	public static final Class<?>[] DEFINE_PARAMS_NAME_BYTE_DOMAIN = new Class<?>[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class };
	public static final Class<?>[] DEFINE_PARAMS_NAME_BUFFER_DOMAIN = new Class<?>[] { String.class, ByteBuffer.class, ProtectionDomain.class };

	/**
	 * Replaces calls to Method.invoke( Object, Object... ), so that reflection code can also be altered.  The current implementation watches for calls to defineClass
	 * and applies Chlorine's class transformation code.
	 * 
	 * @param method
	 * @param target
	 * @param parameters
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
  public static Object invoke( Method method, Object target, Object... parameters ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	  if( method.getDeclaringClass() == ClassLoader.class && method.getName().equals("defineClass") && method.isAccessible() ) {
		  Class<?>[] paramTypes = method.getParameterTypes();
		  if( Arrays.equals(DEFINE_PARAMS_BYTE, paramTypes) ) {
			  byte[] transBytes = transformBytes((byte[])parameters[0], (Integer)parameters[1], (Integer)parameters[2], new ClassByteLoader((ClassLoader)target));
			  return method.invoke(target, transBytes, 0, transBytes.length);
		  }
		  else if( Arrays.equals(DEFINE_PARAMS_NAME_BYTE, paramTypes) ) {
			  byte[] transBytes = transformBytes((byte[])parameters[1], (Integer)parameters[2], (Integer)parameters[3], new ClassByteLoader((ClassLoader)target));
			  return method.invoke(target, parameters[0], transBytes, 0, transBytes.length);
		  }
		  else if( Arrays.equals(DEFINE_PARAMS_NAME_BYTE_DOMAIN, paramTypes) ) {
			  byte[] transBytes = transformBytes((byte[])parameters[1], (Integer)parameters[2], (Integer)parameters[3], new ClassByteLoader((ClassLoader)target));
			  return method.invoke(target, parameters[0], transBytes, 0, transBytes.length, parameters[4]);
		  }
		  else if( Arrays.equals(DEFINE_PARAMS_NAME_BUFFER_DOMAIN, paramTypes) ) {
			  ByteBuffer b = (ByteBuffer)parameters[1];
		      byte[] newBytes = new byte[b.remaining()];
			  b.get(newBytes);
			  ByteBuffer transBuffer = ByteBuffer.wrap(transformBytes((byte[])newBytes, 0, newBytes.length, new ClassByteLoader((ClassLoader)target)));
			  return method.invoke(target, parameters[0], transBuffer, parameters[2]);
		  }
		  throw new InvocationTargetException(new IllegalStateException("Unexpected parameter types for defineClass: "+Arrays.toString(paramTypes)));
	  }
	  else {
		  return method.invoke(target, parameters);
	  }
  }
  
  /**
   * Used by invoke to transform class bytes before they are loaded.
   * 
   * @param bytes
   * @param off
   * @param len
   * @param loader
   * @return
   * @throws InvocationTargetException
   */
  private static byte[] transformBytes( byte[] bytes, int off, int len, ClassByteLoader loader )
    throws InvocationTargetException
  {
	  try {
	    return ClassTransformer.transform(bytes, off, len, loader);
	  }
	  catch( Exception e ) {
		  throw new InvocationTargetException(e);
	  }
  }
}
