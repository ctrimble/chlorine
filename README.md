Project Neon - Java Network Engineering
====================

A library for supporting multiple URLStreamHandlerFactory classes in the same JVM.

The Problem with java.net.URLStreamHandlerFactory
-------------------------------------------------

Java has a powerful protocol handling framework, but it is designed as a JVM wide singleton.  When running in a shared environment,
like a servlet container, this prevents applications from defining new protocols, since these protocols would bleed into other
applications sharing the JVM.  To make matters worse, you cannot use shielding class loaders to work around this, since only the
system classloader can load classes from the java.* packages.

How this Project Works
----------------------

This project provides a set of static factory methods for creating java.net.URL instances and a class transformer
that replaces calls to java.net.URL's constructor with these factory methods.  This allows the URLStreamHandlerFactory
to be isolated to a classloader, without requiring modification to the classes in this java.net package.

Using this Project
------------------

This project is not yet mature enough for use.

Inspecting Byte Code
--------------------

  java -classpath asm.jar:asm-util.jar org.objectweb.asm.util.ASMifier java.lang.Runnable

Resources
---------

- [ASM Guide](http://download.forge.objectweb.org/asm/asm4-guide.pdf)
