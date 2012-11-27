package com.github.ctrimble.chlorine.demo.war;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;

public class ContextURLConnection extends URLConnection {

	private ServletContext context;
	private URL contextUrl = null;
	private URLConnection contextUrlConnection = null;

	protected ContextURLConnection(URL url, ServletContext context) {
		super(url);
		this.context = context;
	}

	@Override
	public void connect() throws IOException {
		contextUrl = context.getResource(super.getURL().getPath());
		contextUrlConnection = contextUrl.openConnection();
		contextUrlConnection.setDoInput(super.getDoInput());
		contextUrlConnection.setDoOutput(super.getDoOutput());
		contextUrlConnection.connect();
	}
	
	@Override
	public InputStream getInputStream()
	  throws IOException
	{
		return contextUrlConnection.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream()
	  throws IOException
	{
		return contextUrlConnection.getOutputStream();
	}
}
