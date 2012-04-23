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

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 * 
 * @author Christian Trimble
 */
public class ClassTransformer {
  public static byte[] transform( byte[] source ) {
	  ClassReader cr = new ClassReader(source);
	  ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS+ClassWriter.COMPUTE_FRAMES);
	  CheckClassAdapter cca = new CheckClassAdapter(cw);
	  URLConstructorAdapter adapter = new URLConstructorAdapter(cca);
      cr.accept(adapter, 0);
	  return cw.toByteArray();
  }
}
