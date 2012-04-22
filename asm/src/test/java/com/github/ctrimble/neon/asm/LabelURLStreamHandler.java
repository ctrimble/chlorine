package com.github.ctrimble.neon.asm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.io.IOUtils;

public class LabelURLStreamHandler
  extends URLStreamHandler
{
	String label;
	public LabelURLStreamHandler(String label) {
		this.label = label;
	}

	@Override
	protected URLConnection openConnection(URL url)
	  throws IOException {
		return new LabelURLConnection(url);
	}
	
	public class LabelURLConnection
	  extends URLConnection
	  {

		protected LabelURLConnection(URL url) {
			super(url);
		}

		@Override
		public void connect() throws IOException {
			
		}
		
		@Override
		public InputStream getInputStream() {
			return IOUtils.toInputStream(label+"="+url.getPath());
		}
	  }
}
