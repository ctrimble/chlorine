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