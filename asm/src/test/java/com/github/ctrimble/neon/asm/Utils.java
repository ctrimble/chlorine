package com.github.ctrimble.neon.asm;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Utils {
	
	public static byte[] getResourceBytes( String path ) throws IOException
	{
		return IOUtils.toByteArray(ConstructorInMethodTest.class.getResourceAsStream(path));
	}

}
