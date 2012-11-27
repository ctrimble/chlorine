package com.github.ctrimble.chlorine.demo.war;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import javax.servlet.ServletContext;

public class DemoURLStreamHandlerFactory implements URLStreamHandlerFactory {
	public static final String CONTEXT_PROTOCOL = "context";
	
	private ServletContext context = null;
	
	public DemoURLStreamHandlerFactory( ServletContext context ) {
		this.context = context;
	}

	@Override
	public URLStreamHandler createURLStreamHandler(String protocol) {
		if( CONTEXT_PROTOCOL.equals(protocol) ) 
		  return new ContextURLStreamHandler(context);
		else return null;
	}

}
