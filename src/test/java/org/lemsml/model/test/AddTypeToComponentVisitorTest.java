package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import compiler.parser.LEMSParser;
import compiler.semantic.visitors.AddTypeToComponentVisitor;
import compiler.semantic.visitors.BuildNameComponentTypeMapVisitor;

import extended.Lems;

/**
 * @author borismarin
 *
 */
public class AddTypeToComponentVisitorTest extends BaseTest
{

	private File schema;
	private File pendulumLemsFile;

	@Before
	public void setUp()
	{
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		pendulumLemsFile = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
	}

	@Test
	public void testComponentTypeVisitor() throws Throwable
	{

		LEMSParser parser = new LEMSParser(pendulumLemsFile, schema);
		Lems lemsDocument = parser.parse();

		// Creates the {String name : ComponentType type} HM used during parsing
		BuildNameComponentTypeMapVisitor buildComponentTypeMapVisitor = new BuildNameComponentTypeMapVisitor(lemsDocument);
		lemsDocument.accept(buildComponentTypeMapVisitor);
		// There are 6 ComponentTypes in standalone_pend
		assertEquals(6, lemsDocument.getComponentTypesByNameHM().size());

		// Adds the corresponding ComponentType to each Component
		AddTypeToComponentVisitor addTypeToComponentVisitor = new AddTypeToComponentVisitor(lemsDocument);
		lemsDocument.accept(addTypeToComponentVisitor);
		// The first component in standalone_pend is <Component type = "SimplePendulum" id="pend" ...
		assertEquals("SimplePendulum", lemsDocument.getComponent().get(0).getComponentType().getName());

	}

}
