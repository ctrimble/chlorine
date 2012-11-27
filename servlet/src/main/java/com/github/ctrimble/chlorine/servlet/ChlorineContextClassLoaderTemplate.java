package com.github.ctrimble.chlorine.servlet;

public abstract class ChlorineContextClassLoaderTemplate<R, T extends Throwable> {
	public R contextClassLoader() throws T
	{
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			ClassLoader chlorineClassLoader = ChlorineContextListener.getChlorineClassLoader();
			Thread.currentThread().setContextClassLoader(chlorineClassLoader);
			return withContextClassLoader(chlorineClassLoader);
		}
		finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}
	
    public abstract R withContextClassLoader(ClassLoader classLoader) throws T;
}
