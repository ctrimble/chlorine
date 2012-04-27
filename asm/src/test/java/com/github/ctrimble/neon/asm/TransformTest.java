/**
 *   Copyright 2012 Christian Trimble
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.ctrimble.neon.asm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

@RunWith(value = Parameterized.class)
public class TransformTest {
	public static final String EXAMPLES_PATH = "/com/github/ctrimble/neon/asm/examples";
	public static final String TEST_CLASS_DIR = "target/test-classes";
	
	@Parameters
	 public static Collection<Object[]> parameters() throws URISyntaxException {
		 ArrayList<Object[]> examples = new ArrayList<Object[]>();
		 for( String entry : new File(TEST_CLASS_DIR+EXAMPLES_PATH).list()) {
			 if( entry.contains("Before")) {
				 examples.add(new Object[] { EXAMPLES_PATH+"/"+entry.replaceAll("Before", "<TYPE>") });
			 }
		 }
		 if( examples.size() == 0 ) fail("There were no examples to test.");
		 return examples;
	 }
	 
	 String namePattern;
	 
	 public TransformTest(String namePattern) {
		 this.namePattern = namePattern;
	 }

	@Test
	public void transform()
	  throws Exception
	{
		// do the transform.
		byte[] beforeBytes = Utils.getResourceBytes(namePattern.replaceAll("<TYPE>", "Before"));
		
		byte[] resultBytes = ClassTransformer.transform(beforeBytes, new ClassByteLoader(this.getClass().getClassLoader()));
		
		// create a string out of the result bytes.
		StringWriter resultWriter = new StringWriter();
		ClassReader resultReader = new ClassReader(resultBytes);
		TraceClassVisitor resultTrace = new TraceClassVisitor(new PrintWriter(resultWriter));
		resultReader.accept(resultTrace, 0);
		String resultString = resultWriter.toString();
		
		// build the expected result.
		StringWriter expectedWriter = new StringWriter();
		byte[] afterBytes = Utils.getResourceBytes(namePattern.replaceAll("<TYPE>", "After"));
		ClassReader expectedReader = new ClassReader(afterBytes);
		TraceClassVisitor expectedTrace = new TraceClassVisitor(new PrintWriter(expectedWriter));
		expectedReader.accept(expectedTrace, 0);
		String expectedString = expectedWriter.toString();
		expectedString = expectedString.replaceAll("After", "Before");
		
		assertEquals("Unexpected code from generator.", expectedString, resultString);
	}

}
