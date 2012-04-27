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
