package com.github.ctrimble.neon.asm;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.github.ctrimble.neon.asm.ClassTransformer.URLConstructorAdapter;


@RunWith(value = Parameterized.class)
public class TransformTest {
	
	@Parameters
	 public static Collection<Object[]> parameters() throws URISyntaxException {
		 ArrayList<Object[]> examples = new ArrayList<Object[]>();
		 URL exampleUrls = TransformTest.class.getResource("examples");
		 File file = new File(exampleUrls.toURI());
		 for( String entry : file.list()) {
			 if( entry.endsWith("Before.java")) {
				 examples.add(new Object[] { "/com/github/ctrimble/neon/asm/examples/"+entry.replaceAll("Before.java$", "") });
			 }
		 }
		 return examples;
	 }
	 
	 String baseName;
	 
	 public TransformTest(String baseName) {
		 this.baseName = baseName;
	 }

	@Test
	public void transform()
	  throws Exception
	{
		// do the transform.
		byte[] beforeBytes = Utils.getResourceBytes(baseName+"Before.class");
		byte[] resultBytes = ClassTransformer.transform(beforeBytes);
		
		// create a string out of the result bytes.
		StringWriter resultWriter = new StringWriter();
		ClassReader resultReader = new ClassReader(resultBytes);
		TraceClassVisitor resultTrace = new TraceClassVisitor(new PrintWriter(resultWriter));
		resultReader.accept(resultTrace, 0);
		String resultString = resultWriter.toString();
		
		// build the expected result.
		StringWriter expectedWriter = new StringWriter();
		byte[] afterBytes = Utils.getResourceBytes(baseName+"After.class");
		ClassReader expectedReader = new ClassReader(afterBytes);
		TraceClassVisitor expectedTrace = new TraceClassVisitor(new PrintWriter(expectedWriter));
		expectedReader.accept(expectedTrace, 0);
		String expectedString = expectedWriter.toString();
		expectedString = expectedString.replaceAll("After", "Before");
		
		assertEquals("Unexpected code from generator.", expectedString, resultString);
	}

}
