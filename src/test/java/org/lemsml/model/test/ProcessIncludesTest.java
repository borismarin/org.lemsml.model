package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.compiler.backend.LemsBackend;
import org.lemsml.model.compiler.parser.LEMSParser;
import org.lemsml.model.compiler.parser.XMLUtils;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import ch.qos.logback.classic.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class ProcessIncludesTest extends BaseTest {

	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(ProcessIncludesTest.class);
	private File schema;
	private File include0;
	private Lems lemsDoc;

	@Before
	public void setUp() throws Throwable {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		include0 = getLocalFile("/examples/include-test/include0.xml");
		if (null == this.lemsDoc) {
			LEMSParser parser = new LEMSParser(include0, schema);
			lemsDoc = parser.parse();
		}
	}

	@Test
	public void validate() {
		assertTrue(XMLUtils.validate(include0, schema));
	}

	@Test
	public void testIncludeVisitor() {

		assertEquals(3, lemsDoc.getConstants().size());

		for (ComponentType ct : lemsDoc.getComponentTypes()) {
			logger.info(ct.getName() + " is defined in "
					+ ct.getDefinedIn().getName());
		}
		for (Component c : lemsDoc.getComponents()) {
			logger.info(c.getId() + " is defined in "
					+ c.getDefinedIn().getName());
		}
		for (Constant ctt : lemsDoc.getConstants()) {
			logger.info(ctt.getName() + " is defined in "
					+ ctt.getDefinedIn().getName());
		}

	}

	@Test
	public void testRoundTrip() throws Throwable {
		File tmpFile = File.createTempFile("includes", ".xml");
		LemsBackend backend = new LemsBackend(lemsDoc);
		backend.setKeepIncludes(true);
		backend.generate(tmpFile);
		// TODO: assert
		System.out.println(Files.toString(tmpFile, Charsets.UTF_8));

		Diff d = DiffBuilder.compare(Input.fromFile(include0))
	             .withTest(tmpFile).build();
		assertFalse(d.hasDifferences());

		backend.setKeepIncludes(false);
		backend.generate(tmpFile);
		// TODO: assert
		System.out.println(Files.toString(tmpFile, Charsets.UTF_8));

	}

}
