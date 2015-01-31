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

import parser.LemsParser;
import parser.LemsXmlUtils;
import parser.XmlFileUtils;
import extended.Lems;

public class SimplePendulumTest extends BaseTest {

	private File schema;
	private File lemsdoc;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		lemsdoc = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
	}

	@Test
	public void validate() {
		assertTrue(XmlFileUtils.validate(lemsdoc, schema));
	}

	@Test
	public void testUnmarshalling() {

		Lems lems = LemsXmlUtils.unmarshall(lemsdoc, schema);
		ComponentType pendCompType = lems.getComponentType().get(0);

		String desc = pendCompType.getDescription();
		assertEquals(desc,
				"Equations of motion for a simple pendulum with mass _m_ and length _l_ ");

		List<Parameter> ParameterList = pendCompType.getParameter();
		assertEquals(ParameterList.get(0).getDescription(), "Mass of the bob");
	}

	@Test
	public void testDimensions() throws Throwable {

		LemsParser parser = new LemsParser(lemsdoc, schema);
		parser.processDimensions();
		assertEquals(parser.getLems().getNameToDimension().get("time"),
				SECOND.getDimension());
		assertEquals(
				parser.getLems().getNameToDimension().get("angular_momentum"),
				SQUARE_METRES_PER_SECOND.multiply(KILOGRAM).getDimension());
	}

}
