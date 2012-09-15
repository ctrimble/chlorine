package com.github.ctrimble.chlorine.asm.isolated;

/**
 * An isolated class loader that must have classes loaded into it using reflection for the package com.github.ctrimble.chlorine.asm.isolated.
 * 
 * @author Christian Trimble
 *
 */
public class IsolatedExternalDefineClassLoader
  extends ClassLoader
{
	public IsolatedExternalDefineClassLoader( ClassLoader parent ) {
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

}
