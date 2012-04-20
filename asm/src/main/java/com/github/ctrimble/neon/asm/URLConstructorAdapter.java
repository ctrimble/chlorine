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

import org.objectweb.asm.ClassVisitor;
import static org.objectweb.asm.Opcodes.ASM4;

/**
 * A class visitor that translates all calls to new java.net.URL(...) into calls to com.github.ctrimble.neon.service.URLFactory.newURL(...).
 *
 * @author Christian Trimble
 */
public class URLConstructorAdapter
  extends ClassVisitor
{
	public URLConstructorAdapter(ClassVisitor cv) {
		super(ASM4, cv);
	}
}
