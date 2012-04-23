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

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.NEW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * A class visitor that translates all calls to new java.net.URL(...) into calls to com.github.ctrimble.neon.service.URLFactory.newURL(...).  This class does not
 * repair the Maxs or Frames, so you will need to use it with the ClassWriter settings ClassWriter.COMPUTE_MAXS+ClassWriter.COMPUTE_FRAMES.
 *
 * @author Christian Trimble
 */
public class URLConstructorAdapter
extends ClassVisitor
{
	/** The method descriptions for new URL() that do not take a URLStreamHandler. */
    private static List<String> newUrlDescs = Arrays.asList("(Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V");
    /** The name of java.net.URL. */
    private static String URL_NAME = "java/net/URL";
    /** The name of the new method. */
    private static String INIT_NAME = "<init>";
    /** The name of the factory. */
    private static String URL_FACTORY_NAME = "com/github/ctrimble/neon/URLFactory";
    /** The method name for the factory methods. */
    private static String NEW_URL_METHOD_NAME = "newURL";

  public URLConstructorAdapter( ClassVisitor next ) {
	  super(ASM4, next);
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
	  return new URLMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
  }


/**
 * Replaces all calls to new URL(), that do not take a URLStreamHandler, into NetIsonlationService.newURL() calls.
 * 
 * @author Christian Trimble
 */
public static class URLMethodVisitor
  extends MethodVisitor {
    
    //private ArrayList<AbstractInsnNode> insnNodes = new ArrayList<AbstractInsnNode>();
    private CompositeInsnNode currentNode = null;
    
	public URLMethodVisitor(MethodVisitor next) {
	  super(ASM4, next);
	}
	
	public void visitTypeInsn(int opcode, String type)
	{
		if( opcode == NEW && URL_NAME.equals(type) ) currentNode = new CompositeInsnNode(currentNode);
		if( currentNode != null ) currentNode.childList.add(new TypeInsnNode(opcode, type));
		else mv.visitTypeInsn(opcode, type);
	}
	
	public void visitInsn(int opcode) {
		if( currentNode != null ) currentNode.childList.add(new InsnNode(opcode));
		else mv.visitInsn(opcode);
	}
	
	@Override
	public void visitLdcInsn(Object cst) {
		if( currentNode != null ) currentNode.childList.add(new LdcInsnNode(cst));
		else mv.visitLdcInsn(cst);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		if( currentNode != null ) currentNode.childList.add(new VarInsnNode(opcode, var));
		else mv.visitVarInsn(opcode, var);
	}
	
	public void visitMethodInsn(int opcode, String owner, String name, String desc)
    {
		// if this is a URL constructor call, then we need to translate it.
		if( opcode == INVOKESPECIAL && URL_NAME.equals(owner) && INIT_NAME.equals(name) ) {
			if( currentNode == null ) throw new IllegalStateException();
			
			// this only translates the init methods that do not take a URLStreamHandler.
			if(newUrlDescs.contains(desc)) {
				currentNode.childList.subList(0, 2).clear();
				currentNode.childList.add(new MethodInsnNode(INVOKESTATIC, URL_FACTORY_NAME, NEW_URL_METHOD_NAME, desc.replaceAll("V$", "Ljava/net/URL;")));
			}
			
			// dump the output, if this is the root node.
			if( currentNode.parent == null ) currentNode.accept(mv);
			
			// set the current node to the parent.
			currentNode = currentNode.parent;
		}
		else if( currentNode != null ) currentNode.childList.add(new MethodInsnNode(opcode, owner, name, desc));
		else mv.visitMethodInsn(opcode, owner, name, desc);
    }

	
	//
	// These methods are not expected in the bytecodes related to creating URLs.
	//
	
	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		if( currentNode != null ) unexpectedBytecode();
		return mv.visitAnnotation(arg0, arg1);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		if( currentNode != null ) unexpectedBytecode();
		return mv.visitAnnotationDefault();
	}

	@Override
	public void visitAttribute(Attribute arg0) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitAttribute(arg0);
	}

	@Override
	public void visitCode() {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitCode();
	}

	@Override
	public void visitEnd() {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitEnd();
	}

	@Override
	public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitFieldInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3,
			Object[] arg4) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitFrame(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void visitIincInsn(int arg0, int arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitIincInsn(arg0, arg1);
	}

	@Override
	public void visitIntInsn(int arg0, int arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitIntInsn(arg0, arg1);
	}

	@Override
	public void visitInvokeDynamicInsn(String arg0, String arg1,
			Handle arg2, Object... arg3) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitInvokeDynamicInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitJumpInsn(int arg0, Label arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitJumpInsn(arg0, arg1);
	}

	@Override
	public void visitLabel(Label arg0) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitLabel(arg0);
	}

	@Override
	public void visitLineNumber(int arg0, Label arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitLineNumber(arg0, arg1);
	}

	@Override
	public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitLocalVariable(arg0, arg1, arg2, arg3, arg4, arg5);
	}


	@Override
	public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitLookupSwitchInsn(arg0, arg1, arg2);
	}

	@Override
	public void visitMaxs(int arg0, int arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitMaxs(arg0, arg1);
	}

	@Override
	public void visitMultiANewArrayInsn(String arg0, int arg1) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitMultiANewArrayInsn(arg0, arg1);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
		if( currentNode != null ) unexpectedBytecode();
		return mv.visitParameterAnnotation(arg0, arg1, arg2);
	}

	@Override
	public void visitTableSwitchInsn(int arg0, int arg1, Label arg2,Label... arg3) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
	}

	@Override
	public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
		if( currentNode != null ) unexpectedBytecode();
		mv.visitTryCatchBlock(arg0, arg1, arg2, arg3);
	}

	
	private static final void unexpectedBytecode() {
		throw new IllegalStateException("Unexpected bytecode encountered during new URL bytecodes.");
	}
	
    static class CompositeInsnNode
	  extends AbstractInsnNode
	  {
        public List<AbstractInsnNode> childList = new ArrayList<AbstractInsnNode>();
		public CompositeInsnNode parent;
        
		protected CompositeInsnNode(CompositeInsnNode parent) {
			super(Opcodes.NOP);
			this.parent = parent;
			if( parent != null ) parent.childList.add(this);
		}

		@Override
		public void accept(MethodVisitor mv) {
			for( AbstractInsnNode node : childList ) node.accept(mv);
		}

		@Override
		public AbstractInsnNode clone(@SuppressWarnings("rawtypes") Map labels) {
			throw new UnsupportedOperationException("The CompositeInsnNode only allows accept(MethodVisitor mv)");
		}

		@Override
		public int getType() {
			throw new UnsupportedOperationException("The CompositeInsnNode only allows accept(MethodVisitor mv)");
		}
		
		@Override
		public AbstractInsnNode getNext() {
			throw new UnsupportedOperationException("The CompositeInsnNode only allows accept(MethodVisitor mv)");
		}
		
		@Override
		public AbstractInsnNode getPrevious() {
			throw new UnsupportedOperationException("The CompositeInsnNode only allows accept(MethodVisitor mv)");
		}
		
	  }
	  
}
}