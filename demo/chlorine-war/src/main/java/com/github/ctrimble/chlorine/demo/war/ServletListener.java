package com.github.ctrimble.chlorine.demo.war;

import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		URL.setURLStreamHandlerFactory(new DemoURLStreamHandlerFactory(event.getServletContext()));
	}

}
