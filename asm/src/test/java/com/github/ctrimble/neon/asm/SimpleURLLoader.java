package com.github.ctrimble.neon.asm;

import java.net.URL;

import org.apache.commons.io.IOUtils;

public class SimpleURLLoader
  implements URLLoader
{
	public String loadUrl(String spec) throws Exception {
		return IOUtils.toString(new URL(spec));
	}
}
