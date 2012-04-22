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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ClassTransformer {
  public static byte[] transform( byte[] source ) {
	  //StringWriter beforeWriter = new StringWriter();
	  //StringWriter afterWriter = new StringWriter();
	  ClassReader cr = new ClassReader(source);
	  ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS+ClassWriter.COMPUTE_FRAMES);
	  //TraceClassVisitor after = new TraceClassVisitor(cw, new PrintWriter(afterWriter));
	  CheckClassAdapter cca = new CheckClassAdapter(cw);
	  URLConstructorAdapter adapter = new URLConstructorAdapter(cca);
	  //TraceClassVisitor before = new TraceClassVisitor(adapter, new PrintWriter(beforeWriter));
      cr.accept(adapter, 0);
      
      //System.out.println("############BEFORE#################");
      //System.out.println(beforeWriter.toString());
      //System.out.println("############After#################");
      //System.out.println(afterWriter.toString());
	  return cw.toByteArray();
  }
  
  public static class URLConstructorAdapter
    extends ClassVisitor
    {
	  public URLConstructorAdapter( ClassVisitor next ) {
		  super(ASM4, next);
	  }
	  
	  @Override
	  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)  {
		  return new URLMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
	  }
    }
  
    /**
     * 
     * @author Christian Trimble
     *
     */
    public static class URLMethodVisitor
      extends MethodVisitor {
        private static List<String> newUrlDescs = Arrays.asList("(Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V");
        private static String URL_DESC = "java/net/URL";
        private static String INIT_NAME = "<init>";
        private static String NET_ISOLATION_SERVICE_DESC = "com/github/ctrimble/neon/service/NetIsolationService";
        private static String NEW_URL_METHOD_NAME = "newURL";
        
		private boolean inNew = false;
        private ArrayList<AbstractInsnNode> insnNodes = new ArrayList<AbstractInsnNode>();
        
		public URLMethodVisitor(MethodVisitor next) {
    	  super(ASM4, next);
		}
		
		public void visitTypeInsn(int opcode, String type)
		{
			if( inNew ) unexpectedBytecode();
			if( opcode == NEW && URL_DESC.equals(type) ) {
				System.out.println("visitTypeInsn");
				insnNodes.add(new TypeInsnNode(opcode, type));
				inNew = true;
			}
			else mv.visitTypeInsn(opcode, type);
		}
		
		public void visitInsn(int opcode) {
			if( inNew ) {
				System.out.println("visitInsn");
				insnNodes.add(new InsnNode(opcode));
			}
			else mv.visitInsn(opcode);
		}
		
		@Override
		public void visitLdcInsn(Object cst) {
			if( inNew ) {
				System.out.println("visitLdcIns");
				insnNodes.add(new LdcInsnNode(cst));
			}
			else mv.visitLdcInsn(cst);
		}
		
		@Override
		public void visitVarInsn(int opcode, int var) {
			if( inNew ) {
				System.out.println("visitVarInsn");
				insnNodes.add(new VarInsnNode(opcode, var));
			}
			else mv.visitVarInsn(opcode, var);
		}
		
		public void visitMethodInsn(int opcode, String owner, String name, String desc)
        {
			// if this is a URL constructor call, then we need to translate it.
			if( opcode == INVOKESPECIAL && URL_DESC.equals(owner) && INIT_NAME.equals(name) ) {
				System.out.println("visitMethodInsn for "+desc);
				if( !inNew ) unexpectedBytecode();
				
				// this only translates the init methods that do not take a URLStreamHandler.  The first 2 insns are
				// visitTypeInsn(NEW, "java/net/URL") and visitInsn(DUP).  We need to drop those.
				if(newUrlDescs.contains(desc)) {
					for( AbstractInsnNode insnNode : insnNodes.subList(2, insnNodes.size())) insnNode.accept(mv);
					mv.visitMethodInsn(INVOKESTATIC, NET_ISOLATION_SERVICE_DESC, NEW_URL_METHOD_NAME, desc.replaceAll("V$", "Ljava/net/URL;"));
				}
				
				// this is one of the URLStreamHandler methods, pass all instructions through.
				else for( AbstractInsnNode insnNode : insnNodes ) insnNode.accept(mv);
				
				// This new method is done, clean up.
				insnNodes.clear();
				inNew = false;
			}
			else {
				if( inNew ) {
				    System.out.println("visitMethodInsn pass through for "+desc);
					insnNodes.add(new InsnNode(opcode));
					inNew = false;
				}
				mv.visitMethodInsn(opcode, owner, name, desc);
			}
        }

		
		//
		// These methods are not expected in the bytecodes related to creating URLs.
		//
    	
    	@Override
		public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
			if( inNew ) unexpectedBytecode();
			return mv.visitAnnotation(arg0, arg1);
		}

		@Override
		public AnnotationVisitor visitAnnotationDefault() {
			if( inNew ) unexpectedBytecode();
			return mv.visitAnnotationDefault();
		}

		@Override
		public void visitAttribute(Attribute arg0) {
			if( inNew ) unexpectedBytecode();
			mv.visitAttribute(arg0);
		}

		@Override
		public void visitCode() {
			if( inNew ) unexpectedBytecode();
			mv.visitCode();
		}

		@Override
		public void visitEnd() {
			if( inNew ) unexpectedBytecode();
			mv.visitEnd();
		}

		@Override
		public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3) {
			if( inNew ) unexpectedBytecode();
			mv.visitFieldInsn(arg0, arg1, arg2, arg3);
		}

		@Override
		public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3,
				Object[] arg4) {
			if( inNew ) unexpectedBytecode();
			mv.visitFrame(arg0, arg1, arg2, arg3, arg4);
		}

		@Override
		public void visitIincInsn(int arg0, int arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitIincInsn(arg0, arg1);
		}

		@Override
		public void visitIntInsn(int arg0, int arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitIntInsn(arg0, arg1);
		}

		@Override
		public void visitInvokeDynamicInsn(String arg0, String arg1,
				Handle arg2, Object... arg3) {
			if( inNew ) unexpectedBytecode();
			mv.visitInvokeDynamicInsn(arg0, arg1, arg2, arg3);
		}

		@Override
		public void visitJumpInsn(int arg0, Label arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitJumpInsn(arg0, arg1);
		}

		@Override
		public void visitLabel(Label arg0) {
			if( inNew ) unexpectedBytecode();
			mv.visitLabel(arg0);
		}

		@Override
		public void visitLineNumber(int arg0, Label arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitLineNumber(arg0, arg1);
		}

		@Override
		public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5) {
			if( inNew ) unexpectedBytecode();
			mv.visitLocalVariable(arg0, arg1, arg2, arg3, arg4, arg5);
		}


		@Override
		public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
			if( inNew ) unexpectedBytecode();
			mv.visitLookupSwitchInsn(arg0, arg1, arg2);
		}

		@Override
		public void visitMaxs(int arg0, int arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitMaxs(arg0, arg1);
		}

		@Override
		public void visitMultiANewArrayInsn(String arg0, int arg1) {
			if( inNew ) unexpectedBytecode();
			mv.visitMultiANewArrayInsn(arg0, arg1);
		}

		@Override
		public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2) {
			if( inNew ) unexpectedBytecode();
			return mv.visitParameterAnnotation(arg0, arg1, arg2);
		}

		@Override
		public void visitTableSwitchInsn(int arg0, int arg1, Label arg2,Label... arg3) {
			if( inNew ) unexpectedBytecode();
			mv.visitTableSwitchInsn(arg0, arg1, arg2, arg3);
		}

		@Override
		public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3) {
			if( inNew ) unexpectedBytecode();
			mv.visitTryCatchBlock(arg0, arg1, arg2, arg3);
		}

		
		private static final void unexpectedBytecode() {
			throw new IllegalStateException("Bytecode encountered during new URL bytecodes.");
		}
    }
}
