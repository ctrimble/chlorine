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

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * A class visitor that modifies the class hierarchy for classes that extend the base class loaders defined by the JDK.
 * 
 * @author Christian Trimble
 *
 */
public class ClassLoaderAdapter extends ClassVisitor {
    private static Map<String, String> superNameMap = new HashMap<String, String>();
    private static String CHLORINE_CLASSLOADER_PACKAGE = "com/github/ctrimble/chlorine/classloader/";
    static {
    	superNameMap.put("java/lang/ClassLoader", "com/github/ctrimble/chlorine/classloader/ChlorineClassLoader");
    	superNameMap.put("java/security/SecureClassLoader", "com/github/ctrimble/chlorine/classloader/ChlorineSecureClassLoader");
    	superNameMap.put("java/net/URLClassLoader", "com/github/ctrimble/chlorine/classloader/ChlorineURLClassLoader");
    	// TODO: add the two classloaders from javax.management
    }
	public ClassLoaderAdapter(ClassVisitor next) {
		super(ASM4, next);
	}
	
	private String newSuperName = null;
	private String oldSuperName = null;

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if( name.startsWith("java/") || name.startsWith("javax/") ||  name.startsWith(CHLORINE_CLASSLOADER_PACKAGE) )
			super.visit(version, access, name, signature, superName, interfaces);
		else if( superNameMap.containsKey(superName) ) {
			oldSuperName = superName;
			newSuperName = superNameMap.get(superName);
			super.visit(version, access, name, signature, newSuperName, interfaces);
		}
		else
		  super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public void visitEnd() {
		newSuperName = null;
		oldSuperName = null;
		super.visitEnd();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if( newSuperName != null && name.equals("<init>"))
		  return new ConstructorVisitor(super.visitMethod(access, name, desc, signature, exceptions));
		else return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	public class ConstructorVisitor
	  extends MethodVisitor
	  {
		public ConstructorVisitor(MethodVisitor mv) {
			super(ASM4, mv);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			if( opcode == INVOKESPECIAL &&  oldSuperName.equals(owner) && name.equals("<init>"))
				mv.visitMethodInsn(opcode, newSuperName, name, desc);
			else super.visitMethodInsn(opcode, owner, name, desc);
		}
	  }
}
