package com.github.ctrimble.neon.asm.examples;

import java.net.MalformedURLException;
import java.net.URL;


public class NestedBefore {
  public URL nested(String spec) throws MalformedURLException {
	  return new URL(new URL(spec).toExternalForm());
  }
  
  public URL nested(String protocol, String host, String path) throws MalformedURLException {
	  return new URL(new URL(protocol, host, path).toExternalForm());
  }
  
  public URL nested(String protocol, String host, int port, String path) throws MalformedURLException {
	  return new URL(new URL(protocol, host, port, path).toExternalForm());
  }
  
  public URL addProtocol(String spec) throws MalformedURLException {
	  return new URL("other:"+new URL(spec).toExternalForm());
  }
  
  public URL addProtocol(String protocol, String host, String path) throws MalformedURLException {
	  return new URL("other:"+new URL(protocol, host, path).toExternalForm());
  }

  public URL addProtocol(String protocol, String host, int port, String path) throws MalformedURLException {
	  return new URL("other:"+new URL(protocol, host, port, path).toExternalForm());
  }
}
