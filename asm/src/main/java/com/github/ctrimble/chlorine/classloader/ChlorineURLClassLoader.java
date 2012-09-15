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
package com.github.ctrimble.chlorine.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import com.github.ctrimble.chlorine.asm.ClassByteLoader;
import com.github.ctrimble.chlorine.asm.ClassTransformer;

public class ChlorineURLClassLoader extends URLClassLoader implements ChlorineSecureClassDefiner {
	private ClassByteLoader loader;

	public ChlorineURLClassLoader(URL[] urls) {
		super(urls);
		loader = new ClassByteLoader(this);
	}

	public ChlorineURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		loader = new ClassByteLoader(this);
	}

	public ChlorineURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
		loader = new ClassByteLoader(this);
	}
	
	@SuppressWarnings("deprecation")
	public Class<?> chlorineDefineClass(byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(newBytes, 0, newBytes.length);
	}

	public Class<?> chlorineDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length);
	}

	public Class<?> chlorineDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name,  newBytes, 0, newBytes.length, protectionDomain);
	}

	public Class<?> chlorineDefineClass(String name, ByteBuffer b,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, protectionDomain);
	}

	public Class<?> chlorineDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
		byte[] newBytes = ClassTransformer.transform(b, off, len, loader);
		return defineClass(name, newBytes, 0, newBytes.length, cs);
	}

	public Class<?> chlorineDefineClass(String name, ByteBuffer b, CodeSource cs) {
		byte[] newBytes = new byte[b.remaining()];
	    b.get(newBytes);
		return defineClass(name, newBytes, 0, newBytes.length, cs);
	}
}
