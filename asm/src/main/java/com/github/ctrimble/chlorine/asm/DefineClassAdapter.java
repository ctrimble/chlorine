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
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.GETFIELD;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * A class visitor for ASM 4 that injects Chlorine class loaders into java class files.
 * 
 * @author Christian Trimble
 */
public class DefineClassAdapter extends ClassVisitor {
    private static String CHLORINE_CLASSLOADER_PACKAGE = "com/github/ctrimble/chlorine/classloader/";
    private static String CHLORINE_DEFINE_CLASS_NAME = "chlorineDefineClass";
    private static String CLASS_LOADER_NAME = "java/lang/ClassLoader";
    private static String SECURE_CLASS_LOADER_NAME = "java/security/SecureClassLoader";
    private static String URL_CLASS_LOADER_NAME = "java/net/URLClassLoader";
    private static String CHLORINE_SECURE_CLASS_LOADER_NAME = "com/github/ctrimble/chlorine/classloader/ChlorineSecureClassLoader";
    private static String CHLORINE_CLASS_LOADER_NAME = "com/github/ctrimble/chlorine/classloader/ChlorineClassLoader";
    private static String CHLORINE_URL_CLASS_LOADER_NAME = "com/github/ctrimble/chlorine/classloader/ChlorineURLClassLoader";
    private static Set<String> DEFINE_CLASS_DESCS = new HashSet<String>();
    private static Set<String> DEFINE_CLASS_SECURE_DESCS = new HashSet<String>();
    static {
    	DEFINE_CLASS_DESCS.add("([BII)Ljava/lang/Class;");
    	DEFINE_CLASS_DESCS.add("(Ljava/lang/String;[BII)Ljava/lang/Class;");
    	DEFINE_CLASS_DESCS.add("(Ljava/lang/String;[BIILjava/security/ProtectionDomain;)Ljava/lang/Class;");
    	DEFINE_CLASS_DESCS.add("(Ljava/lang/String;Ljava/nio/ByteBuffer;Ljava/security/ProtectionDomain;)Ljava/lang/Class;");
    	// TODO: get the SecureClassLoader defineClass descs in here.
    	//DEFINE_CLASS_SECURE_DESCS
    	//DEFINE_CLASS_SECURE_DESCS
    }

	private ClassReader source;
    private ClassByteLoader loader;
	public DefineClassAdapter(ClassReader source, ClassByteLoader loader, ClassVisitor next) {
		super(ASM4, next);
		this.source = source;
		this.loader = loader;
	}
	
    private Boolean isClassLoader = null;
    private Boolean isSecureClassLoader = null;
    private Boolean isURLClassLoader = null;
    
    private Boolean isClassLoader() {
    	if( isClassLoader == null ) isClassLoader = CLASS_LOADER_NAME.equals(source.getClassName()) || hasAncestor( source, loader, CLASS_LOADER_NAME);
    	return isClassLoader;
    }
    
    private Boolean isSecureClassLoader() {
    	if( isSecureClassLoader == null ) isSecureClassLoader = SECURE_CLASS_LOADER_NAME.equals(source.getClassName()) || hasAncestor( source, loader, SECURE_CLASS_LOADER_NAME);
    	return isSecureClassLoader;
    }
    
    public Boolean isURLClassLoader() {
    	if( isURLClassLoader == null ) isURLClassLoader = URL_CLASS_LOADER_NAME.equals(source.getClassName()) || hasAncestor( source, loader, URL_CLASS_LOADER_NAME);
    	return isURLClassLoader;    	
    }
    
    private static boolean hasAncestor( ClassReader reader, ClassByteLoader loader, String internalName ) {
    	String superName = reader.getSuperName();
    	if( superName.equals(internalName) ) return true;
    	else if( superName.equals("java/lang/Object") ) return false;
    	else return hasAncestor( new ClassReader(loader.getBytes(superName)), loader, internalName);
    }
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      if(name.startsWith(CHLORINE_CLASSLOADER_PACKAGE)) return super.visitMethod(access, name, desc, signature, exceptions);
      else return new DefineClassVisitor(super.visitMethod(access, name, desc, signature, exceptions));
	}
	
	/**
	 * An ASM 4 class visitor that replaces instantiations of java class loaders with Chlorine class loaders.
	 * 
	 * @author Christian Trimble
	 */
	public class DefineClassVisitor
	    extends MethodVisitor
	{
		public DefineClassVisitor(MethodVisitor mv) {
			super(ASM4, mv);
		}
		
		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			if( opcode == INVOKEVIRTUAL && "defineClass".equals(name) &&
		        ( (DEFINE_CLASS_DESCS.contains(desc) && isClassLoader() ) ||
		          (DEFINE_CLASS_SECURE_DESCS.contains(desc) && isSecureClassLoader() ) ) ) {
				if( isURLClassLoader() )
					mv.visitMethodInsn(opcode, CHLORINE_URL_CLASS_LOADER_NAME, CHLORINE_DEFINE_CLASS_NAME, desc);
				else if( isSecureClassLoader() )
				    mv.visitMethodInsn(opcode, CHLORINE_SECURE_CLASS_LOADER_NAME, CHLORINE_DEFINE_CLASS_NAME, desc);
				else if( isClassLoader() ) {
					mv.visitMethodInsn(opcode, CHLORINE_CLASS_LOADER_NAME, CHLORINE_DEFINE_CLASS_NAME, desc);
				}
			}
			else super.visitMethodInsn(opcode, owner, name, desc);
		}

	}
}
