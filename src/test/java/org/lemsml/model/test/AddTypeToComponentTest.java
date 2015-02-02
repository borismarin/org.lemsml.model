package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import compiler.parser.LEMSParser;
import compiler.semantic.visitors.AddTypeToComponent;
import compiler.semantic.visitors.BuildNameToCompTypeMap;

import extended.Lems;

/**
 * @author borismarin
 *
 */
public class AddTypeToComponentTest extends BaseTest
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
		BuildNameToCompTypeMap buildComponentTypeMapVisitor = new BuildNameToCompTypeMap(lemsDocument);
		lemsDocument.accept(buildComponentTypeMapVisitor);
		// There are 6 ComponentTypes in standalone_pend
		assertEquals(6, lemsDocument.getNameToCompType().size());

		// Adds the corresponding ComponentType to each Component
		AddTypeToComponent addTypeToComponent = new AddTypeToComponent(lemsDocument);
		lemsDocument.accept(addTypeToComponent);
		// The first component in standalone_pend is <Component type = "SimplePendulum" id="pend" ...
		assertEquals("SimplePendulum", lemsDocument.getComponent().get(0).getComponentType().getName());

	}

}
