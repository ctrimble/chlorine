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
package com.github.ctrimble.chlorine.asm;

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
