package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.lemsml.model.compiler.backend.JavaDomainModelBackend;

import com.google.common.io.Files;

public class JavaDomainModelBackendTest {

	@Test
	public void test() throws Throwable {
		File outDir = Files.createTempDir();
		File compDefs = new File(getClass().getResource("/examples/java-domain-model-test/FooML.xml").getFile());
		JavaDomainModelBackend be = new JavaDomainModelBackend("fooml", compDefs, outDir);
		be.generate();
		assertEquals(8, new File(outDir, "org/fooml/model").listFiles().length);
	}

}
