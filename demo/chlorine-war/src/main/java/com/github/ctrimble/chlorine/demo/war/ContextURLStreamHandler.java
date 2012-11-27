package com.github.ctrimble.chlorine.demo.war;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.servlet.ServletContext;

public class ContextURLStreamHandler extends URLStreamHandler {
	
	private ServletContext context;
	
	public ContextURLStreamHandler( ServletContext context ) {
		this.context = context;
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return new ContextURLConnection(url, context);
	}

}
