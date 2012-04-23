package com.github.ctrimble.neon.asm.examples;

import java.net.MalformedURLException;
import java.net.URL;


public class GeneralBefore {
	public static final String SPEC = "http://ctrimble.github.com/neon";
	public static final String PROTOCOL = "http";
	public static final String HOST = "ctrimble.github.com";
	public static final int PORT = -1;
	public static final String PATH = "/neon";
	
  public static URL staticSpecURL = null;
  public static URL staticProtocolHostPathURL = null;
  public static URL staticProtocolHostPortPathURL = null;

  static {
	  try {
		  staticSpecURL = new URL(SPEC);
		  staticProtocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
		  staticProtocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
	  }
	  catch( Exception e ) {
		  e.printStackTrace();
	  }
  }
  
  public URL specURL = null;
  public URL protocolHostPathURL = null;
  public URL protocolHostPortPathURL = null;
  
  public GeneralBefore() throws MalformedURLException {
	  specURL = new URL(SPEC);
	  protocolHostPathURL  = new URL(PROTOCOL, HOST, PATH);
	  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
  }
  
  public void specURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(SPEC);
	  specURL = new URL(SPEC);
  }
  
  public void protocolHostPathURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(PROTOCOL, HOST, PATH);
	  protocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
  }
  
  public void protocolHostPortPathURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(PROTOCOL, HOST, PORT, PATH);
	  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
  }
  
  public static void staticSpecURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(SPEC);  
	  staticSpecURL = new URL(SPEC);  
  }
  
  public static void staticProtocolHostPathURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(PROTOCOL, HOST, PATH);
	  staticProtocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
  }
  
  public static void staticProtocolHostPortPathURL() throws MalformedURLException
  {
	  @SuppressWarnings("unused")
	  URL url = new URL(PROTOCOL, HOST, PORT, PATH);
	  staticProtocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
  }
  
  public class Inner {
	  public Inner() throws MalformedURLException {
		  specURL = new URL(SPEC);
		  protocolHostPathURL  = new URL(PROTOCOL, HOST, PATH);
		  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);		  
	  }
	  
	  public void specURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(SPEC);
		  specURL = new URL(SPEC);
	  }
	  
	  public void protocolHostPathURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(PROTOCOL, HOST, PATH);
		  protocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
	  }
	  
	  public void protocolHostPortPathURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(PROTOCOL, HOST, PORT, PATH);
		  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
	  }
  }
  
  public static class StaticInner {
	  public static URL staticSpecURL = null;
	  public static URL staticProtocolHostPathURL = null;
	  public static URL staticProtocolHostPortPathURL = null;

	  static {
		  try {
			  staticSpecURL = new URL(SPEC);
			  staticProtocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
			  staticProtocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
		  }
		  catch( Exception e ) {
			  e.printStackTrace();
		  }
	  }
	  
	  public URL specURL = null;
	  public URL protocolHostPathURL = null;
	  public URL protocolHostPortPathURL = null;
	  
	  public StaticInner() throws MalformedURLException {
		  specURL = new URL(SPEC);
		  protocolHostPathURL  = new URL(PROTOCOL, HOST, PATH);
		  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);		  
	  }
	  
	  public void specURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(SPEC);
		  specURL = new URL(SPEC);
	  }
	  
	  public void protocolHostPathURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(PROTOCOL, HOST, PATH);
		  protocolHostPathURL = new URL(PROTOCOL, HOST, PATH);
	  }
	  
	  public void protocolHostPortPathURL() throws MalformedURLException
	  {
		  @SuppressWarnings("unused")
		  URL url = new URL(PROTOCOL, HOST, PORT, PATH);
		  protocolHostPortPathURL = new URL(PROTOCOL, HOST, PORT, PATH);
	  }
  }
  
}
