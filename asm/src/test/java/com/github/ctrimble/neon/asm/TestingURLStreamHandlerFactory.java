package com.github.ctrimble.neon.asm;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class TestingURLStreamHandlerFactory implements URLStreamHandlerFactory {

	public URLStreamHandler createURLStreamHandler(String protocol) {
		System.out.println("Getting handler for protocol:"+protocol);
		if( "label".equals(protocol)) {
			System.out.println("Returning Label handler");
		  return new LabelURLStreamHandler("TESTING");
		}
		else {
			System.out.println("Returning null.");
			return null;
		}
	}

}
