package com.github.ctrimble.chlorine.asm.isolated;

import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.github.ctrimble.chlorine.asm.URLLoader;

public class IsolatedURLLoader
  implements URLLoader
{
	public String loadUrl(String spec) throws Exception {
		return IOUtils.toString(new URL(spec));
	}
}
