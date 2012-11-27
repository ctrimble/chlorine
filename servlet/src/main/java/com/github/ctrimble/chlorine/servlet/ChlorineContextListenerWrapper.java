package com.github.ctrimble.chlorine.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ChlorineContextListenerWrapper implements ServletContextListener {
	private String contextListenerClassName;
	private ServletContextListener contextListener;

	public ChlorineContextListenerWrapper(String contextListenerClassName) {
		this.contextListenerClassName = contextListenerClassName;
	}

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		new ChlorineContextClassLoaderTemplate<Void, RuntimeException>() {
			@Override
			public Void withContextClassLoader(ClassLoader classLoader) throws RuntimeException {
				if( contextListener != null ) contextListener.contextDestroyed(event);
				return null;
			}
			
		}.contextClassLoader();
    }

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		new ChlorineContextClassLoaderTemplate<Void, RuntimeException>() {
			@Override
			public Void withContextClassLoader(ClassLoader classLoader) throws RuntimeException {
				try {
					contextListener = (ServletContextListener)classLoader.loadClass(contextListenerClassName).newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Could not create servlet context listener "+contextListenerClassName, e);
				}
				contextListener.contextInitialized(event);
				return null;
			}
			
		}.contextClassLoader();
	}

}
