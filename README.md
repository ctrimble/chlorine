Project Chlorine
====================

A Java ClassLoader framework for cleaning up JVM singleton services.

The problem with some JVM features
----------------------------------

The JVM is often used in situations where multiple distinct applications are deployed.  Unfortunately, some features available
to applications assume that there is only one application running.  For example, Java provides a powerful protocol handling
framework, but there is no way to use it in a per application manner.  This project aims to provide a general framework
for dealing with these problems and some solutions for particular problems.

How this project works
----------------------

This project deals with JVM level problems by providing a way to dynamically replace these frameworks in already compiled 
code.  This makes the solution transparent to the JVM "fixes" being applied.  All that the target application needs to do
is load their applications classes through Chlorine and it will clean up the mess.

Cleaning up java.net.URLStreamHandlerFactory
-------------------------------------------------

Java has a powerful protocol handling framework, but it is designed as a JVM wide singleton.  When running in a shared environment,
like a servlet container, this prevents applications from defining new protocols, since these protocols would bleed into other
applications sharing the JVM.  To make matters worse, you cannot use shielding class loaders to work around this, since only the
system classloader can load classes from the java.* packages.

This project provides a set of static factory methods for creating java.net.URL instances and a class transformer
that replaces calls to java.net.URL's constructor with these factory methods.  This allows the URLStreamHandlerFactory
to be isolated to a classloader, without requiring modification to the classes in the java.net package.

Using this project
------------------

This project is not yet mature enough for use.

Inspecting byte code
--------------------

  java -classpath asm.jar:asm-util.jar org.objectweb.asm.util.ASMifier java.lang.Runnable

Resources
---------

- [ASM Guide](http://download.forge.objectweb.org/asm/asm4-guide.pdf)
