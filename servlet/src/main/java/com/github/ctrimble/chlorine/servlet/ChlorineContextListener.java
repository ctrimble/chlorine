package com.github.ctrimble.chlorine.servlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.ctrimble.chlorine.classloader.ChlorineURLClassLoader;

public class ChlorineContextListener implements ServletContextListener {
	private static ClassLoader chlorineClassLoader = null;
	public static final String CHLORINE_LIB_RESOURCE_PATH = "com.github.ctrimble.chlorine.servlet.libResourcePath";
	public static final String DEFAULT_LIB_RESOURCE_PATH = "/WEB-INF/chlorine-lib";
	
	public static ClassLoader getChlorineClassLoader() {
		return chlorineClassLoader;
	}
	
	String libResourcePath;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
        chlorineClassLoader = null;
        libResourcePath = null;
    }

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		libResourcePath = (String)context.getAttribute(CHLORINE_LIB_RESOURCE_PATH);
		if( libResourcePath == null ) libResourcePath = DEFAULT_LIB_RESOURCE_PATH;
		
		ArrayList<URL> libUrls = new ArrayList<URL>();
		for( String libPath : context.getResourcePaths(libResourcePath) ) {
			if( libPath.endsWith(".jar") ) {
				try {
					libUrls.add(context.getResource(libPath));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		chlorineClassLoader = new ChlorineURLClassLoader(libUrls.toArray(new URL[libUrls.size()]));
	}

}
