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
package com.github.ctrimble.chlorine.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 * 
 * @author Christian Trimble
 */
public class ClassTransformer {
	
	  public static byte[] transform( byte[] source, ClassByteLoader loader  ) {
		  return transform(source, 0, source.length, loader);
	  }
	  
  public static byte[] transform( byte[] source, int off, int len, ClassByteLoader loader ) {
	  ClassReader cr = new ClassReader(source);
	  ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS+ClassWriter.COMPUTE_FRAMES);
	  ClassVisitor cv = createVisitor(cr, loader, cw);
      cr.accept(cv, 0);
	  return cw.toByteArray();
  }
  
  public static ClassVisitor createVisitor(ClassReader cr, ClassByteLoader loader, ClassVisitor cv) {
	  CheckClassAdapter cleckClass = new CheckClassAdapter(cv);
	  URLConstructorAdapter urlConstructor = new URLConstructorAdapter(cleckClass);
	  ClassLoaderAdapter classLoader = new ClassLoaderAdapter(urlConstructor);
	  DefineClassAdapter defineClass = new DefineClassAdapter(cr, loader, classLoader); 
	  ReflectionAdapter reflectionAdapter = new ReflectionAdapter(defineClass);
	  return reflectionAdapter;
  }
}
