package com.github.ctrimble.neon.asm.examples;

import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.github.ctrimble.neon.asm.URLLoader;

public class SimpleURLLoaderBefore
  implements URLLoader
{
	public String loadUrl(String spec) throws Exception {
		return IOUtils.toString(new URL(spec));
	}
}
