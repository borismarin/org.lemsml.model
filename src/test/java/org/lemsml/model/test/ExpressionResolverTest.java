package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
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

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(lemsDoc,
				schema);
		Lems compiledLems = compiler.generateLEMSDocument();

		assertEquals("-0.1", compiledLems.getConstantByName("const0").getValue());
	}

}
