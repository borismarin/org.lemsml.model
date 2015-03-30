package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.compiler.parser.LEMSParser;
import org.lemsml.model.compiler.parser.XMLUtils;
import org.lemsml.model.extended.Lems;

public class ExpressionResolverTest extends BaseTest {

	private File schema;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
	}


	@Test
	public void testNested() throws Throwable {

		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		LEMSParser parser = new LEMSParser(lemsDoc, schema);
		assertTrue(XMLUtils.validate(lemsDoc, schema));
		Lems parsedLems = parser.parse();

		System.out.println(parsedLems.getConstants().get(0).getValue());
		assertEquals(-0.1, parsedLems.getConstants().get(0).getValue().getValue(), 1e-6);
	}

}
