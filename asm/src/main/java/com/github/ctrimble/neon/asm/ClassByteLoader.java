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

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ClassByteLoader {
	ClassLoader source = null;
	
	public ClassByteLoader( ClassLoader source ) {
		this.source = source;
	}
	
	public byte[] getBytes( String name ) throws ClassInspectionException {
		try {
		  return IOUtils.toByteArray(source.getResourceAsStream(name+".class"));
		}
		catch( IOException ioe ) {
			throw new ClassInspectionException("Could not load bytes for class "+name, ioe);
		}
	}
}
