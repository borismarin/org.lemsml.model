package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import parser.LemsParser;
import parser.XmlFileUtils;

public class IncludesVisitorTest extends BaseTest {

	private File schema;
	private File include0;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		include0 = getLocalFile("/examples/include0.xml");
	}

	@Test
	public void validate() {
		assertTrue(XmlFileUtils.validate(include0, schema));
	}

	@Test
	public void testUnmarshalling() throws Throwable {

		LemsParser parser = new LemsParser(include0, schema);
		parser.processIncludes();
		assertEquals(parser.getLems().getConstant().size(), 3);
		System.out.println(parser.getLems().getConstant().get(0).getValue());
	}

}
