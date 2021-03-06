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

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

/**
 * Chlorine specific replacements for the defineClass methods provided by java.lang.ClassLoader.
 * 
 * @author Christian Trimble
 */
public interface ChlorineClassDefiner {
	Class<?> chlorineDefineClass(byte[] b, int off, int len)
			throws ClassFormatError;

	Class<?> chlorineDefineClass(String name, byte[] b, int off, int len)
			throws ClassFormatError;
	
	Class<?> chlorineDefineClass(String name, byte[] b, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError;

	Class<?> chlorineDefineClass(String name, ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError;
}
