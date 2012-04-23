package com.github.ctrimble.neon.asm.examples;

import java.net.MalformedURLException;
import java.net.URL;
import static com.github.ctrimble.neon.service.NetIsolationService.newURL;

public class NestedAfter {
  public URL nested(String spec) throws MalformedURLException {
	  return newURL(newURL(spec).toExternalForm());
  }
  
  public URL nested(String protocol, String host, String path) throws MalformedURLException {
	  return newURL(newURL(protocol, host, path).toExternalForm());
  }
  
  public URL nested(String protocol, String host, int port, String path) throws MalformedURLException {
	  return newURL(newURL(protocol, host, port, path).toExternalForm());
  }
  
  public URL addProtocol(String spec) throws MalformedURLException {
	  return newURL("other:"+newURL(spec).toExternalForm());
  }
  
  public URL addProtocol(String protocol, String host, String path) throws MalformedURLException {
	  return newURL("other:"+newURL(protocol, host, path).toExternalForm());
  }

  public URL addProtocol(String protocol, String host, int port, String path) throws MalformedURLException {
	  return newURL("other:"+newURL(protocol, host, port, path).toExternalForm());
  }
}