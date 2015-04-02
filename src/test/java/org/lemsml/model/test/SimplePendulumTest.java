package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tec.units.ri.util.SI.KILOGRAM;
import static tec.units.ri.util.SI.SECOND;
import static tec.units.ri.util.SI.SQUARE_METRES_PER_SECOND;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.compiler.parser.LEMSXMLReader;
import org.lemsml.model.compiler.parser.XMLUtils;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class SimplePendulumTest extends BaseTest
{

	private File schema;
	private File pendLemsFile;
	private Lems compiledLems;

	@Before
	public void setUp() throws Throwable
	{
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		pendLemsFile = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(pendLemsFile, schema);
		compiledLems = compiler.generateLEMSDocument();
	}

	@Test
	public void validate()
	{
		assertTrue(XMLUtils.validate(pendLemsFile, schema));
	}

	@Test
	public void testUnmarshalling()
	{

		Lems lems = LEMSXMLReader.unmarshall(pendLemsFile, schema);
		ComponentType pendCompType = lems.getComponentTypes().get(0);

		String desc = pendCompType.getDescription();
		assertEquals(desc, "Equations of motion for a simple pendulum with mass _m_ and length _l_ ");

		List<Parameter> ParameterList = pendCompType.getParameters();
		assertEquals(ParameterList.get(0).getDescription(), "Mass of the bob");
		
	}

	@Test
	public void testDecoration() throws Throwable
	{
		assertEquals(compiledLems.getComponentById("pend").getParameterValue("l").getValue(), 1.0, 1e-6);
	}

	@Test
	public void testDimensions() throws Throwable
	{

		assertEquals(compiledLems.getNameToDimension().get("time").getDimension(), SECOND.getDimension());
		assertEquals(compiledLems.getNameToDimension().get("angular_momentum").getDimension(), SQUARE_METRES_PER_SECOND.multiply(KILOGRAM).getDimension());
	}

}
