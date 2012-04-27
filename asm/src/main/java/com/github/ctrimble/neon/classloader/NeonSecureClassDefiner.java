package com.github.ctrimble.neon.classloader;

import java.nio.ByteBuffer;
import java.security.CodeSource;

public interface NeonSecureClassDefiner
  extends NeonClassDefiner {
    Class<?> neonDefineClass(String name, byte[] b, int off, int len, CodeSource cs);
    Class<?> neonDefineClass(String name, ByteBuffer b, CodeSource cs);
}
