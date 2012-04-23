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

import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.github.ctrimble.neon.asm.URLLoader;

@SuppressWarnings("unused")
public class SimpleURLLoaderAfter
  implements URLLoader
{
	public String loadUrl(String spec) throws Exception {
		return IOUtils.toString(com.github.ctrimble.neon.URLFactory.newURL(spec));
	}
}
