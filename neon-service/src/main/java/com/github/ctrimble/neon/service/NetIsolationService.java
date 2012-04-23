/**
 *   Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.ctrimble.neon.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.net.ContentHandlerFactory;
/**
 * The class loader isolated replacement for java.net.URL constructors.
 * 
 * @author Christian Trimble
 */
public class NetIsolationService {
	  private static URLStreamHandlerFactory streamFactory = null;
	  private static ContentHandlerFactory contentHandlerFactory = null;
	  
	  public static void setURLStreamHandlerFactory( URLStreamHandlerFactory streamFactory ) {
		  NetIsolationService.streamFactory = streamFactory;
	  }
	  
	  public static URLStreamHandlerFactory getURLStreamHandlerFactory() {
		  return streamFactory;
	  }
	  
	  public static void setContentHandlerFactory( ContentHandlerFactory contentHandlerFactory ) {
		  NetIsolationService.contentHandlerFactory = contentHandlerFactory;
	  }
	  
	  public static ContentHandlerFactory getContentHandlerFactory() {
		  return contentHandlerFactory;
	  }
	   
	  /**
	   * A replacement for the new java.net.URL( String spec ) constructor.
	   * 
	   * @param spec
	   * @return
	 * @throws MalformedURLException 
	   */
	   public static URL newURL( String spec ) throws MalformedURLException
	   {
		   return newURL( null, spec );
	   }
	   
	   /**
	    * A replacement for the new java.net.URL( URL, String ) constructor.
	    * 
	    * @param context
	    * @param spec
	    * @return
	 * @throws MalformedURLException 
	    */
	   public static URL newURL( URL context, String spec ) throws MalformedURLException
	   {
		   URLStreamHandlerFactory factory = streamFactory;
		   if( factory == null ) return new URL( context, spec );
	     return new URL(context, spec, factory.createURLStreamHandler(spec.substring(0, spec.indexOf(':'))));
	   }
	
	   /**
	    * A replacement for the new java.net.URL( String, String, String ) constructor.
	    * 
	    * @param protocol
	    * @param host
	    * @param file
	    * @return
	 * @throws MalformedURLException 
	    */
	   public static URL newURL( String protocol, String host, String file ) throws MalformedURLException {
		   return newURL( protocol, host, -1, file );
	   }
	   
	   /**
	    * A replacement for the new java.net.URL( String, String, int, String ) constructor.
	    * 
	    * @param protocol
	    * @param host
	    * @param port
	    * @param file
	    * @return
	 * @throws MalformedURLException 
	    */
	   public static URL newURL( String protocol, String host, int port, String file) throws MalformedURLException
	   {
		   URLStreamHandlerFactory factory = streamFactory;
		   if( factory == null ) return new URL( protocol, host, port, file );
	     return new URL( protocol, host, port, file, factory.createURLStreamHandler( protocol ) );
	   }
}
