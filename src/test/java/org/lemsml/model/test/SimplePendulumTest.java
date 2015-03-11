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
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class SimplePendulumTest extends BaseTest
{

	private File schema;
	private File pendLemsFile;

	@Before
	public void setUp()
	{
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		pendLemsFile = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
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
		ComponentType pendCompType = lems.getComponentType().get(0);

		String desc = pendCompType.getDescription();
		assertEquals(desc, "Equations of motion for a simple pendulum with mass _m_ and length _l_ ");

		List<Parameter> ParameterList = pendCompType.getParameter();
		assertEquals(ParameterList.get(0).getDescription(), "Mass of the bob");
	}

	@Test
	public void testDimensions() throws Throwable
	{

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(pendLemsFile, schema);
		Lems lemsDoc = compiler.generateLEMSDocument();
		assertEquals(lemsDoc.getNameToDimension().get("time").getDimension(), SECOND.getDimension());
		assertEquals(lemsDoc.getNameToDimension().get("angular_momentum").getDimension(), SQUARE_METRES_PER_SECOND.multiply(KILOGRAM).getDimension());
	}

}
