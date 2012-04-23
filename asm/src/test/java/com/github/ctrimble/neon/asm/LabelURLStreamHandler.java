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
